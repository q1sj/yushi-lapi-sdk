package com.xsy.yushi.lapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Q1sj
 * @date 2026/4/12 下午1:09
 */
@Getter
@AllArgsConstructor
public enum ColorEnum {
	BLACK(0, "黑色"),
	WHITE(1, "白色"),
	GRAY(2, "灰色"),
	RED(3, "红色"),
	BLUE(4, "蓝色"),
	YELLOW(5, "黄色"),
	ORANGE(6, "橙色"),
	BROWN(7, "棕色"),
	GREEN(8, "绿色"),
	PURPLE(9, "紫色"),
	CYAN(10, "青色"),
	PINK(11, "粉色"),
	TRANSPARENT(12, "透明"),
	SILVER(13, "银白"),
	DARK(14, "深色"),
	LIGHT(15, "浅色"),
	COLORLESS(16, "无色"),
	YELLOW_GREEN(17, "黄绿双色"),
	GRADIENT_GREEN(18, "渐变绿色"),
	OTHER(99, "其他"),
	UNKNOWN(100, "未知");
	private final int value;
	private final String desc;

	public static ColorEnum fromValue(int value) {
		for (ColorEnum color : values()) {
			if (color.value == value) {
				return color;
			}
		}
		return UNKNOWN;
	}
}
