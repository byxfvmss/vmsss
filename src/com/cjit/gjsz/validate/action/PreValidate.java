package com.cjit.gjsz.validate.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PreValidate {
	// 5秒钟检查一次
	private static final int seconds = 5;

	private String validateFilePath;// ---指定的外管局要校验的文件夹

	private List errorList;// ----反馈回来的错误信息LIST

	Timer timer;

	private List validateFileList;// ---要校验的文件LIST

	private String validateFeedBack;// --FeedBack文件夹

	public List getValidateFileList() {
		return validateFileList;
	}

	public void setValidateFileList(List validateFileList) {
		this.validateFileList = validateFileList;
	}

	public PreValidate() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new PreValidateTask(), seconds * 1000,
				seconds * 1000);
	}

	public String getValidateFeedBack() {
		return validateFeedBack;
	}

	public void setValidateFeedBack(String validateFeedBack) {
		this.validateFeedBack = validateFeedBack;
	}

	class PreValidateTask extends TimerTask {
		// @Override
		public void run() {
			System.out.println("预校验开始");
			PreValidateUtil preValidateUtil = new PreValidateUtil(
					validateFilePath, validateFileList);
			preValidateUtil.setValidateFeedBack(validateFeedBack);
			try {
				// --如果文件夹为空，那么就开始做去校验后的取数据工作；
				int m = preValidateUtil.startPreValidate();
				if (m > 0) {
					// ---开始执行取数据的工作
					errorList = new ArrayList();
					// ---取数据开始
					errorList = preValidateUtil.readErrorList();
					// ---取数据结束
					timer.cancel();

				} else if (m <= 0) {
					// ---继续等待，取数据
					System.out.println("等待校验完成...");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				timer.cancel();
			}
			preValidateUtil.deleteLockFile(validateFilePath);// ---解除锁定
		}
	}

	public String getValidateFilePath() {
		return validateFilePath;
	}

	public void setValidateFilePath(String validateFilePath) {
		this.validateFilePath = validateFilePath;
	}

	public List getErrorList() {
		return errorList;
	}

	public void setErrorList(List errorList) {
		this.errorList = errorList;
	}

}
