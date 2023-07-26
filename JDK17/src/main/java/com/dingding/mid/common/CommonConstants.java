
package com.dingding.mid.common;

/**
 */
public interface CommonConstants {
	/** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
	Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
	/** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
	Integer SC_OK_200 = 200;

	String FORM_VAR="formData";
	String PROCESS_STATUS="processStatus";
	String START_USER_INFO="startUser";
	String BUSINESS_STATUS_1="1"; //正在处理
	String BUSINESS_STATUS_2="2";//撤销
	String BUSINESS_STATUS_3="3";//驳回
	String BUSINESS_STATUS_4="4";//已结束

}
