package com.xsy.yushi.lapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Q1sj
 * @date 2026/4/12 上午9:35
 */
@Getter
@AllArgsConstructor
public enum ImageDataTypeEnum {
	BASE64_DATA_TYPE(0),
	URL_DATA_TYPE(1),
	CLOUD_DATA_TYPE(2);
	private final int value;

	public static ImageDataTypeEnum fromValue(int value) {
		for (ImageDataTypeEnum e : values()) {
			if (e.value == value) {
				return e;
			}
		}
		return null;
	}
}
