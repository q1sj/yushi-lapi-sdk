package com.uni.beans;

import lombok.Data;

/**
 * @author Q1sj
 * @date 2026/4/12 上午10:11
 */
@Data
public class DeviceInfo {
		private int ID;
		private String DeviceName;
		private int DeviceType;
		private int DeviceCode;
		private String DeviceModel;
		private String SerialNumber;
		private String FirmwareVersion;
		private String HardwareID;
		private String UbootVersion;
}
