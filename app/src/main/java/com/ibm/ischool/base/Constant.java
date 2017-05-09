package com.ibm.ischool.base;

public class Constant {

	public static  final Boolean DEBUG = false;

	public static final String APP_FOLDER_NAME = "ISchool";
	
	public static final String BASE_SERVER_URL = "http://60.205.178.158/ischool_server";
	
	/**
	 * 合格分数
	 */
	public static final Double STANDARD_SCORE = 0.2;
	
	/**
	 * 合格音节
	 */
	public static final Double STANDARD_SYLLABELS = 0.3;
	
	/**
	 * 每个关卡的Lesson个数
	 */
	public static final Integer TOTAL_LESSON = 9;
	
	/**
	 * 每个Level的关卡数
	 */
	public static final Integer TOTAL_CHAPTER = 10;

	public static final String LEARNING_MODE = "learn_mode";

	/**
	 * 闯关模式
	 */
	public static final Integer PASS_MODE = 0;

	/**
	 * 跟读模式
	 */
	public static final Integer TRACKING_MODE = 1;
}
