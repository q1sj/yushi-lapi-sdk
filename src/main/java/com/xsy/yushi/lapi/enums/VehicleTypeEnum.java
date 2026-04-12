package com.xsy.yushi.lapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Q1sj
 * @date 2026/4/12 下午1:13
 */
@Getter
@AllArgsConstructor
public enum VehicleTypeEnum {
	TRICYCLE(0, "三轮车"),
	BUS(1, "大客车"),
	SMALL_CAR(2, "小型车"),
	MEDIUM_CAR(3, "中型车"),
	LARGE_CAR(4, "大型车"),
	BICYCLE(5, "二轮车"),
	MOTORCYCLE(6, "摩托车"),
	TRACTOR(7, "拖拉机"),
	FARM_TRUCK(8, "农用货车"),
	SEDAN(9, "轿车"),
	SUV(10, "SUV"),
	VAN(11, "面包车"),
	SMALL_TRUCK(12, "小货车"),
	MINIBUS(13, "中巴车"),
	LARGE_BUS(14, "大客车/大型客车"),
	HEAVY_TRUCK(15, "大货车"),
	PICKUP(16, "皮卡车"),
	BUSINESS_CAR(17, "商务车"),
	SPORTS_CAR(18, "跑车"),
	MICRO_SEDAN(19, "微型轿车"),
	HATCHBACK(20, "两厢轿车"),
	SEDAN_THREE_BOX(21, "三厢轿车"),
	LIGHT_BUS(22, "轻型客车"),
	MEDIUM_TRUCK(23, "中型货车"),
	TRAILER(24, "挂车"),
	TANKER_TRUCK(25, "槽罐车"),
	WATER_TRUCK(26, "洒水车"),
	UNKNOWN(998, "未知"),
	OTHER(999, "其它");

	private final int value;
	private final String desc;

	public static VehicleTypeEnum fromValue(int value) {
		for (VehicleTypeEnum type : values()) {
			if (type.value == value) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
