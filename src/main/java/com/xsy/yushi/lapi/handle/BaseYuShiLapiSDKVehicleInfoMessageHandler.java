package com.xsy.yushi.lapi.handle;

import com.alibaba.fastjson.JSON;
import com.xsy.yushi.lapi.config.YuShiLapiConfig;
import com.xsy.yushi.lapi.enums.ColorEnum;
import com.xsy.yushi.lapi.enums.VehicleTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * @author Q1sj
 * @date 2026/4/10 下午4:29
 */
@Slf4j
public abstract class BaseYuShiLapiSDKVehicleInfoMessageHandler extends BaseYuShiLapiSDKStructureMessageHandler {


	protected abstract void handle(YuShiLapiConfig config, Vehicle vehicle);

	protected abstract String saveImage(YuShiLapiConfig config, Vehicle vehicle, ImageInfo imageInfo) throws IOException;

	@Override
	public void handle(YuShiLapiConfig config, String message) {
		StructureMessage structureMessage = JSON.parseObject(message, StructureMessage.class);
		StructureInfo structureInfo = structureMessage.getStructureInfo();
		if (structureInfo == null) {
			log.warn("structureInfo为空");
			return;
		}
		List<VehicleInfo> vehicleInfos = Optional.of(structureMessage).map(StructureMessage::getStructureInfo)
				.map(StructureInfo::getObjInfo)
				.map(ObjInfo::getVehicleInfoList)
				.orElse(Collections.emptyList());
		for (VehicleInfo vehicleInfo : vehicleInfos) {
			Vehicle vehicle = new Vehicle();
			// 抓拍时间
			vehicle.setPassTime(Optional.ofNullable(vehicleInfo).map(VehicleInfo::getPassTime)
					.map(passTime -> {
						try {
							return DateUtils.parseDate(passTime, "yyyyMMddHHmmssSSS");
						} catch (ParseException e) {
							log.warn("宇视LAPI卡口抓拍时间:{}解析失败 使用当前时间", passTime);
							return new Date();
						}
					}).orElseGet(Date::new));
			// 车辆颜色
			vehicle.setCarColor(Optional.ofNullable(vehicleInfo)
					.map(VehicleInfo::getVehicleAttributeInfo)
					.map(VehicleAttributeInfo::getColor)
					.map(ColorEnum::fromValue)
					.orElse(ColorEnum.UNKNOWN));
			// 车辆类型
			vehicle.setCarType(Optional.ofNullable(vehicleInfo)
					.map(VehicleInfo::getVehicleAttributeInfo)
					.map(VehicleAttributeInfo::getType)
					.map(VehicleTypeEnum::fromValue)
					.orElse(VehicleTypeEnum.UNKNOWN));
			// 车牌号
			vehicle.setCarNo(Optional.ofNullable(vehicleInfo)
					.map(VehicleInfo::getPlateAttributeInfo)
					.map(PlateAttributeInfo::getPlateNo)
					.orElse(""));
			// 车牌颜色
			vehicle.setCarNoColor(Optional.ofNullable(vehicleInfo)
					.map(VehicleInfo::getPlateAttributeInfo)
					.map(PlateAttributeInfo::getColor)
					.map(ColorEnum::fromValue)
					.orElse(ColorEnum.UNKNOWN));
			List<String> images = new ArrayList<>();
			if (structureInfo.getImageNum() > 0) {
				for (ImageInfo imageInfo : structureInfo.getImageInfoList()) {
					try {
						String image = saveImage(config, vehicle, imageInfo);
						if (StringUtils.isNotBlank(image)) {
							images.add(image);
						}
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
			vehicle.setImages(images);
			handle(config, vehicle);
		}
	}


	@Data
	public static class Vehicle {
		private Date passTime;
		private String carNo;
		private ColorEnum carNoColor;
		private VehicleTypeEnum carType;
		private ColorEnum carColor;
		private List<String> images;
	}
}
