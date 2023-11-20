package com.example.questionnaire.Service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.questionnaire.Service.ifs.QuizService;
import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
//import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizVo;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuestionnaireDao qnDao;
	@Autowired
	private QuestionDao quDao;

	@Override
	@Transactional // ��2��save���ন�\save���ɭԤ~�|save�A�u��[�bpublic �W��
	public QuizRes create(QuizReq req) {
		// �s�W�ݨ�
		// �ϥ��ˬd��k
		List<QuizVo> quizVoList = new ArrayList<>();
		QuizRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}
		// �x�s��A�� QN ���̷s�@����ID�ԥX�ӡA�s��QU��qn_id���A
		int qnid = qnDao.save(req.getQuestionnaire()).getId();
		List<Question> quList = req.getQuestionList();
		// �i�H�u�s�W�ݨ��A�ݨ����S���D��
		if (quList.isEmpty()) {
			quizVoList.add(req);
			return new QuizRes(quizVoList,RtnCode.SUCCESSFUL);
//			return new QuizRes();
		}

//		int qnid = qnDao.findTopByOrderById().getId();
		for (Question qu : quList) {
			qu.setqnId(qnid);
		}
		quDao.saveAll(req.getQuestionList());
		return new QuizRes(quizVoList,RtnCode.SUCCESSFUL);
	}

	// ���ˬd��k�ԥX�ӥ�
	private QuizRes checkParam(QuizReq req) {
		Questionnaire qn = req.getQuestionnaire();

		if (!StringUtils.hasText(qn.getTitle()) || !StringUtils.hasText(qn.getDescription())
				|| qn.getStartDate() == null || qn.getEndDate() == null || qn.getStartDate().isAfter(qn.getEndDate())) {
			return new QuizRes(RtnCode.QNPARAM_ERROR);
		}
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			if (qu.getQuId() <= 0 || !StringUtils.hasText(qu.getqTitle()) || !StringUtils.hasText(qu.getOptionsType())
					|| !StringUtils.hasText(qu.getOptions())) {
				return new QuizRes(RtnCode.QUPARAM_ERROR);
			}
		}
		// ��null�N���\�A�S����
		return null;
	}

	@Transactional
	@Override
	public QuizRes update(QuizReq req) {
		// ��諸�ɾ� �G ���o��+�w�o���|���}�l
		// ��s�ݨ�
		// �ϥ��ˬd��k
		QuizRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}
		// ��X�ˬdID����k
		checkResult = checkQuid(req);
		if (checkResult != null) {
			return checkResult;
		}
		Optional<Questionnaire> qnOp = qnDao.findById(req.getQuestionnaire().getId());
		if (qnOp.isEmpty()) {
			return new QuizRes(RtnCode.ID_NOTFOUNT);
		}
		// ��諸�ɾ� �G ���o��+�w�o���|���}�l ( ��e�ɶ� < �}�l�ɶ� )
		if (!qnOp.get().isPublished()
				|| (qnOp.get().isPublished() && (LocalDate.now().isBefore(qnOp.get().getStartDate())))) {
			qnDao.save(req.getQuestionnaire());
			quDao.saveAll(req.getQuestionList());
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		return new QuizRes(RtnCode.UPDATE_ERROR);
	}

	private QuizRes checkQuid(QuizReq req) {
		if (req.getQuestionnaire().getId() <= 0) {
			return new QuizRes(RtnCode.ID_ERROR);
		}
		// �P�_QuestionList�̪�quid�O�_����Questionnaire�̪�id
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			if (qu.getqnId() != req.getQuestionnaire().getId()) {
				return new QuizRes(RtnCode.ID_ERROR);
			}
		}

		return null;
	}

	@Override
	public QuizRes deleQuestionnaire(List<Integer> qnIdList) {
		// �R�h�i�ݨ� �u�� �|���o�� + �|���}��(��e��� < �}�l���)
		List<Questionnaire> resList = qnDao.findByIdIn(qnIdList);
		List<Integer> deleList = new ArrayList<>();
		for (Questionnaire qn : resList) {
			if (!qn.isPublished() || qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate())) {
				deleList.add(qn.getId());
				// qnDao.deleteById(qn.getId());
			}
		}
		if (!deleList.isEmpty()) {
			qnDao.deleteAllById(deleList);
			quDao.deleteAllByQnIdIn(deleList);
		}
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes deleQuestion(int qnid, List<Integer> quIdList) {
		List<Question> resList = quDao.findByQuIdInAndQnId(quIdList, qnid);
		List<Integer> idList = new ArrayList<>();
		Optional<Questionnaire> qnOp = qnDao.findById(qnid);
		if (!qnOp.get().isPublished()
				|| qnOp.get().isPublished() && LocalDate.now().isBefore(qnOp.get().getStartDate())) {

			for (Question qu : resList) {
				idList.add(qu.getQuId());
			}
			if (!idList.isEmpty() && qnid > 0) {
				quDao.deleteAllByQnIdInAndQuId(idList, qnid);
			}
		}

		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes search(String title, LocalDate startDate, LocalDate endDate) {
		
		title = StringUtils.hasText(title) ? title: "";
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		
		/* �g���T���� �W��
		if(!StringUtils.hasText(title)) {
			title = "";
		}
		if(startDate == null) {
			startDate = LocalDate.of(1971, 1, 1);
		}
		if(endDate == null) {
			endDate = LocalDate.of(2099, 12, 31);
		}
		*/
		// ������1�B4�B9�i�ݨ��A�A��1�B4�B9�i�ݨ��̪��D�ءA�ðt��
		
		// ���1 4 9 �i�ݨ�
		List<Questionnaire> qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		// ���X 1 4 9 ID
		List<Integer> qnIdList = new ArrayList<>(); 
		for(Questionnaire qn:qnList) {
			qnIdList.add(qn.getId());
		}
		
		// ��� 1 4 9 �i�ݨ��̪��D��
		List<Question> quList = quDao.findAllByQnIdIn(qnIdList);
		
		// �t��ݨ����D�ب� VO vo�̥]�@�i�ݨ�QN �M�Ӱݨ����h���D��list<QU>
		// for�j��@�i�@�i�ݨ��A�̦Afor�j������ݨ����D�ءA��set��ݨ��D��
		// ��쪺��ƻݭn���ӪF��ӸˡA���ެO�ݨ��٬O�D��
		// �ݨ��O�@�D�ҥH���   �D�جOList �ҥH��list �h��
		List<QuizVo> quizVoList = new ArrayList<>();
		for(Questionnaire qn : qnList) {
			QuizVo vo = new QuizVo(); // ����i�ݨ��M�D��
			vo.setQuestionnaire(qn);  // ���ݨ�
			List<Question> quesList = new ArrayList<>();
			for(Question qu:quList) { 
				// ID�۲šA�s�J�����ݨ����D��
				if(qu.getqnId() == qn.getId()) {					
					quesList.add(qu);
				}
			}
			vo.setQuestionList(quesList); // ���D��
			quizVoList.add(vo);  // �N�D�ةM�ݨ��s��VO
		}
		
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes create1(QuizReq req) {
		Questionnaire qn = req.getQuestionnaire();
		List<Question> quList = req.getQuestionList();
		int qnid = qnDao.save(qn).getId();
		for(Question qu:quList) {
			qu.setqnId(qnid);
		}
		quDao.saveAll(quList);
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate, boolean isAll) {
		title = StringUtils.hasText(title) ? title: "";
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		List<Questionnaire> qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		if(isAll) {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(title, startDate, endDate);
		}
		else {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		}
		return new QuestionnaireRes(qnList,RtnCode.SUCCESSFUL);
	}

	@Override
	public QuestionRes searchQuestionList(int qnId) {
		if(qnId <= 0) {
			return new QuestionRes(null,RtnCode.QNPARAM_ERROR);
		}
		List<Question> quList = quDao.findAllByQnIdIn(Arrays.asList(qnId));
		
		return null;
	}


}
