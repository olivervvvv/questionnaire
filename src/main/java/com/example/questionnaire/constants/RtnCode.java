package com.example.questionnaire.constants;

public enum RtnCode {

	// �C�| 200 400 401 403 404
	// �䪺��200 �䤣��404 �ѤU400
	// 401 403 ���v������
	SUCCESSFUL(200, "Successful"), // ���\
	QNPARAM_ERROR(400, "Questionnaire_Param_error"), // �Ѽƿ��~
	QUPARAM_ERROR(400, "Question_Param_error"), // �Ѽƿ��~
	ID_ERROR(400,"Id_error"),//id���~
	ID_NOTFOUNT(404,"ID_Notfount"),//�䤣��ID
	UPDATE_ERROR(400,"Update_error"),
	
//	ACCOUNT_EXISTED(400,"Account_existed"),//�b���w�s�b
//	ACCOUNT_NOTFOUNT(404,"Account_Notfount"),//�䤣��b��
//	BALANCE_IS_INSUFFICIENT(400,"Balance is insufficient"),//�l�B���� 
	;

	private int code;

	private String message;

	// source -->
	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	// �u���Ψ�getter
	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
