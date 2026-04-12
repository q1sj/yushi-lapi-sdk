package com.xsy.yushi.lapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Q1sj
 * @date 2026/4/12 下午1:35
 */
@Getter
@AllArgsConstructor
public enum ImageTypeEnum {
	CAPTURE_PANORAMA_BG(1, "抓拍全景图/背景图"),
	TARGET_SMALL(2, "目标小图"),
	LICENSE_PLATE_BINARY(3, "车牌二值化图"),
	DRIVER_FACE_FEATURE(4, "主驾驶面部特征图"),
	PASSENGER_FACE_FEATURE(5, "副驾驶面部特征图"),
	VIOLATION_COMPOSITE(7, "违章合成图"),
	PASSING_COMPOSITE(8, "过车合成图"),
	VEHICLE_CLOSE_UP(9, "车辆特写图"),
	ID_PHOTO(10, "证件照图片"),
	CAPTURE_FACE_SMALL(11, "抓拍人脸小图"),
	PERSON_SMALL(12, "人体小图"),
	MOTORIZED_VEHICLE_SMALL(13, "机动车小图"),
	NON_MOTORIZED_VEHICLE_SMALL(14, "非机动车小图"),
	STRUCTURED_FACE_LARGE(15, "结构化时人脸大图"),
	STRUCTURED_PERSON_LARGE(16, "结构化时人体大图"),
	STRUCTURED_MOTORIZED_VEHICLE_LARGE(17, "结构化时机动车大图"),
	STRUCTURED_NON_MOTORIZED_VEHICLE_LARGE(18, "结构化时非机动车大图"),
	PEDESTRIAN_CLOSE_UP(19, "行人特写图（行人闯红灯模式）"),
	PEDESTRIAN_FACE_SMALL(20, "人脸小图（行人闯红灯模式）"),
	STRUCTURED_COMBINED_LARGE(23, "结构化时机动车、非机动车、人员、车牌合一大图"),
	LICENSE_PLATE_LARGE(24, "车牌大图"),
	VISIBLE_LIGHT(25, "可见光图片"),
	THERMAL_IMAGING(26, "热成像图片"),
	LINKAGE_CHANNEL_CAPTURE(27, "联动通道抓拍图片"),
	SAFETY_HELMET_LARGE(28, "安全帽大图"),
	SAFETY_HELMET_SMALL(29, "安全帽小图"),
	WORK_UNIFORM_LARGE(30, "工作服大图"),
	WORK_UNIFORM_SMALL(31, "工作服小图");

	private final int value;
	private final String desc;

	public static ImageTypeEnum fromValue(int value) {
		for (ImageTypeEnum e : values()) {
			if (e.value == value) {
				return e;
			}
		}
		return null;
	}
}