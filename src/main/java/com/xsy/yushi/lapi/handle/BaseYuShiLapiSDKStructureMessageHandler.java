package com.xsy.yushi.lapi.handle;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Q1sj
 * @date 2026/4/10 下午4:29
 */
@Slf4j
public abstract class BaseYuShiLapiSDKStructureMessageHandler implements YuShiSDKMessageHandler {


	@Override
	public String supportUri() {
		return "/LAPI/V1.0/System/Event/Notification/Structure";
	}

	/**
	 * Auto-generated: 2026-04-10 16:35:36
	 *
	 * @author bejson.com (i@bejson.com)
	 * @website http://www.bejson.com/java2pojo/
	 */
	@Data
	public static class StructureMessage {

		private String Reference;
		private long TimeStamp;
		private int Seq;
		private int SrcID;
		private String SrcName;
		private int NotificationType;
		private String DeviceID;
		private String RelatedID;
		private StructureInfo StructureInfo;
	}

	/**
	 * Auto-generated: 2026-04-10 16:35:36
	 *
	 * @author bejson.com (i@bejson.com)
	 * @website http://www.bejson.com/java2pojo/
	 */
	@Data
	public static class ComAttachIndexInfoList {

		private int AttachIndex;
		private int ComIndex;

	}

	@Data
	public static class PlateAttributeInfo {

		private int PlateStatus;
		private int PlatePicAttachIndex;
		private int PlateConfidence;
		private String PlateNo;
		private String Position;
		private int Color;
		private int Type;
	}


	/**
	 * Auto-generated: 2026-04-10 16:35:36
	 *
	 * @author bejson.com (i@bejson.com)
	 * @website http://www.bejson.com/java2pojo/
	 */
	@Data
	public static class VehicleAttributeInfo {

		private int Type;
		private int LimitedSpeed;
		private int MarkedSpeed;
		private int SpeedValue;
		private int SpeedUnit;
		private String SimulateFlag;
		private int Color;
		private String DriveStatus;
		private String VehicleBrand;
		private String VehicleBrandType;
		private String VehicleType;
		private String VehicleColor;
		private String VehicleColorDept;
		private int AimStatus;
		private int DriverSunVisorStatus;
		private int CodriverSunVisorStatus;
		private int DriverSeatBeltStatus;
		private int CodriverSeatBeltStatus;
		private int DriverMobileStatus;
		private int TaxiMarkStatus;
		private int ScuttleStatus;
		private int NapkinBoxStatus;
		private int PendantStatus;
		private int DangerousGoodsMarkStatus;
		private int YellowPlateMarkStatus;
	}


	/**
	 * Auto-generated: 2026-04-10 16:35:36
	 *
	 * @author bejson.com (i@bejson.com)
	 * @website http://www.bejson.com/java2pojo/
	 */
	@Data
	public static class VehicleFaceInfo {

		private int IsVehicleHead;
	}


	/**
	 * Auto-generated: 2026-04-10 16:35:36
	 *
	 * @author bejson.com (i@bejson.com)
	 * @website http://www.bejson.com/java2pojo/
	 */
	@Data
	public static class LaneInfo {

		private int ID;
		private String Description;
		private String PlaceCode;
		private String PlaceName;
		private int LaneType;
		private int LaneDirection;
		private String Direction;
		private String DirectionName;

	}

	/**
	 * Copyright 2026 bejson.com
	 */

	@Data
	public static class VehicleInfo {

		private int ID;
		private int VehicleDoforFaceID;
		private int VehicleDoforSubFaceID;
		private String Position;
		private int LargePicAttachIndex;
		private int PlatePicAttachIndex;
		private int ComAttachNum;
		private List<ComAttachIndexInfoList> ComAttachIndexInfoList;
		private String PassTime;
		private int TriggerType;
		private PlateAttributeInfo PlateAttributeInfo;
		private VehicleAttributeInfo VehicleAttributeInfo;
		private VehicleFaceInfo VehicleFaceInfo;
		private LaneInfo LaneInfo;

	}

	/**
	 * Copyright 2026 bejson.com
	 */

	/**
	 * Auto-generated: 2026-04-10 16:35:36
	 *
	 * @author bejson.com (i@bejson.com)
	 * @website http://www.bejson.com/java2pojo/
	 */
	@Data
	public static class ObjInfo {

		private int FaceNum;
		private List<String> FaceInfoList;
		private int PersonNum;
		private List<String> PersonInfoList;
		private int NonMotorVehicleNum;
		private List<String> NonMotorVehicleInfoList;
		private int VehicleNum;
		private List<VehicleInfo> VehicleInfoList;

	}


	/**
	 * Auto-generated: 2026-04-10 16:35:36
	 *
	 * @author bejson.com (i@bejson.com)
	 * @website http://www.bejson.com/java2pojo/
	 */
	@Data
	public static class ImageInfo {

		private int Index;
		private int Type;
		private int Format;
		private int Width;
		private int Height;
		private String CaptureTime;
		private String CaptureTimeStr;
		private int DataType;
		private long Size;
		private String Data;
		private String URL;
	}

	@Data
	public static class ImageComposeInfo {

		private int SubComposeFlag;


	}


	/**
	 * Auto-generated: 2026-04-10 16:35:36
	 *
	 * @author bejson.com (i@bejson.com)
	 * @website http://www.bejson.com/java2pojo/
	 */
	@Data
	public static class StructureInfo {

		private ObjInfo ObjInfo;
		private int ImageNum;
		private List<ImageInfo> ImageInfoList;
		private ImageComposeInfo ImageComposeInfo;

	}
}
