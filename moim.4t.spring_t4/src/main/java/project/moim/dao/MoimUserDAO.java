package project.moim.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import project.moim.dto.BoardList;
import project.moim.dto.Moim;
import project.moim.dto.MoimMem;
import project.moim.dto.MoimTname;
import project.moim.dto.MoimUser;
import project.moim.dto.ReDat;
import project.moim.dto.Schedule;
import project.moim.dto.Schemem;

@Repository
public class MoimUserDAO {
	private @Autowired SqlSessionTemplate sqlSession;
	private final String NSPACE = "spring.4t.moim.";
	
/* 사용자 정보 */
	// 사용자 정보 입력
	public int insertUser(MoimUser moimUser) {
		return sqlSession.insert(NSPACE + "insertUser", moimUser);
	}
	
	// 사용자 정보 수정
	public int updateUser(MoimUser moimUser) {
		return sqlSession.update(NSPACE + "updateUser", moimUser);
	}
	
	// 회원탈퇴
	public int deleteUser(String id) {
		return sqlSession.delete(NSPACE + "deleteUser", id);
	}
	
	// 사용자 정보 조회
	public MoimUser selectUser(String id) {
		return sqlSession.selectOne(NSPACE + "selectUser", id);
	}
	
	// 사용자 프로필 조회
	public String selectUserProf(String id) {
		return sqlSession.selectOne(NSPACE + "selectUserProf", id);
	}
	
/* 모임 멤버 */
	// 입력
	public int insertMem(MoimMem moimMem) {
		return sqlSession.insert(NSPACE + "insertMem", moimMem);
	}
	
	// 수정
	public int updateMem(MoimMem moimMem) {
		return sqlSession.insert(NSPACE + "updateMem", moimMem);
	}
	
	// 모임 멤버 즐찾 수정
	public int updateMemUserFav(MoimMem moimMem) {
		return sqlSession.insert(NSPACE + "updateMemUserFav", moimMem);
	}
	
	// 모임 멤버 권한 수정
	public int updateMemUserPermit(MoimMem moimMem) {
		return sqlSession.insert(NSPACE + "updateMemUserPermit", moimMem);
	}
	
	// 아이디 탈퇴
	public int deleteUserMem(String id) {
		return sqlSession.insert(NSPACE + "deleteUserMem", id);
	}
	
	// 모임 탈퇴
	public int deleteMem(MoimMem moimMem) {
		return sqlSession.insert(NSPACE + "deleteMem", moimMem);
	}
	
	// 모임 삭제
	public int deleteMoimMem(int moimcode) {
		return sqlSession.insert(NSPACE + "deleteMoimMem", moimcode);
	}
	
	// 모임 내 사용자 id 리스트 조회
	public List<MoimMem> selectMoimMem(int moimcode) {
		return sqlSession.selectList(NSPACE + "selectMoimMem", moimcode);
	}
	
	// 모임 내 사용자 id 부가정보 조회
	public MoimMem selectUserMoimMem(MoimMem moimMem) {
		return sqlSession.selectOne(NSPACE + "selectUserMoimMem", moimMem);
	}
	
	// 사용자가 가입한 모임 리스트 조회
	public List<MoimMem> selectUserMoimList(String id) {
		return sqlSession.selectList(NSPACE + "selectUserMoimList", id);
	}
	
	
/* 게시판 */	
	// 게시판 등록
	public int insertBoard(BoardList boardList) {
		return sqlSession.insert(NSPACE + "insertBoard", boardList);
	}
	
	// 글 수정
	public int updateBoard(BoardList boardList) {
		return sqlSession.update(NSPACE + "updateBoard", boardList);
	}
	
	// 글 삭제
		// 특정 글 삭제
	public int deleteBoard(int listnum) {
		return sqlSession.delete(NSPACE + "deleteBoard", listnum);
	}
	
		// 모임 글 전부 삭제
	public int deleteBoardList(int moimcode) {
		return sqlSession.delete(NSPACE + "deleteBoardList", moimcode);
	}
	
		// 사용자 글 전부 삭제
	public int deleteUserBoard(String id) {
		return sqlSession.delete(NSPACE + "deleteUserBoard", id);
	}
	
		// 모임 내 사용자 글 전부 삭제
	public int deleteMoimUserBoard(MoimMem moimMem) {
		return sqlSession.delete(NSPACE + "deleteMoimUserBoard", moimMem);
	}
	
	// 글 조회
		// 모임내 글 전체 조회
	public List<BoardList> selectMoimBoard(int moimcode) {
		return sqlSession.selectList(NSPACE + "selectMoimBoard", moimcode);
	}
	
		// 글 분류에 따른 모임 내의 글 전체 조회
	public List<BoardList> selectBoardList(Map<String, Integer> map) {
		return sqlSession.selectList(NSPACE + "selectBoardList", map);
	}
	
		// 모임 내 필독 글 조회
	public List<BoardList> selectBoardFeel(int moimcode) {
		return sqlSession.selectList(NSPACE + "selectBoardFeel", moimcode);
	}
	
		// 모임 내 일반 게시글 조회
	public List<BoardList> selectBoardNomal(int moimcode) {
		return sqlSession.selectList(NSPACE + "selectBoardNomal", moimcode);
	}
	
		// 특정 글 상세 조회
	public BoardList selectBoard(int listnum) {
		return sqlSession.selectOne(NSPACE + "selectBoard", listnum);
	}
	
		// 모임내 사용자 작성 글 조회
	public List<BoardList> selectMoimUserBoard(MoimMem moimMem) {
		return sqlSession.selectList(NSPACE + "selectMoimUserBoard", moimMem);
	}
	
		// 사용자 작성 글 조회
	public List<BoardList> selectUserBoard(String id) {
		return sqlSession.selectList(NSPACE + "selectUserBoard", id);
	}
	
/* 덧 글 */	
	// 덧글 작성
	public int insertReDat(ReDat reDat) {
		return sqlSession.insert(NSPACE + "insertReDat", reDat);
	}
	
	// 덧글 수정
	public int updateReDat(ReDat reDat) {
		return sqlSession.update(NSPACE + "updateReDat", reDat);
	}
	
	// 덧글 삭제
		// 원글 삭제에 따른 덧글 삭제
	public int deleteBoardReDat(int listnum) {
		return sqlSession.delete(NSPACE + "deleteBoardReDat", listnum);
	}
	
		// 모임 삭제에 따른 덧글 삭제
	public int deleteMoimRedat(int moimcode) {
		return sqlSession.delete(NSPACE + "deleteMoimRedat", moimcode);
	}
	
		// 특정 덧글 삭제
	public int deleteRedat(int renum) {
		return sqlSession.delete(NSPACE + "deleteRedat", renum);
	}
	
		// 사용자 작성 덧글 삭제
	public int deleteUserRedat(String id) {
		return sqlSession.delete(NSPACE + "deleteUserRedat", id);
	}
	
		// 모임내 사용자 덧 글 삭제
	public int deleteMoimUserRedat(MoimMem moimMem) {
		return sqlSession.delete(NSPACE + "deleteMoimUserRedat", moimMem);
	}
	
	// 덧글 조회
		// 특정 덧글 조회
	public ReDat selectRedat(int renum) {
		return sqlSession.selectOne(NSPACE + "selectRedat", renum);
	}
	
		// 원글에 따른 덧글 리스트 조회
	public List<ReDat> selectReDatList(int listnum) {
		return sqlSession.selectList(NSPACE + "selectReDatList", listnum);
	}
	
	
/* 일정 */	
	// 일정 입력
	public int insertSchedule(Schedule schedule) {
		return sqlSession.insert(NSPACE + "insertSchedule", schedule);
	}
	
	// 일정 수정
	public int updateSchedule(Schedule schedule) {
		return sqlSession.update(NSPACE + "updateSchedule", schedule);
	}
	
	// 일정 삭제
		// 모임 일정 전체 삭제
	public int deleteMoimSchedule(int sch_moimcode) {
		return sqlSession.delete(NSPACE + "deleteMoimSchedule", sch_moimcode);
	}
	
		// 단일 일정 삭제
	public int deleteSchedule(int sch_schnum) {
		return sqlSession.delete(NSPACE + "deleteSchedule", sch_schnum);
	}
	
	// 일정 조회
		// 모임 내 전체 일정 조회
	public List<Schedule> selectMoimSchedule(int sch_moimcode) {
		return sqlSession.selectList(NSPACE + "selectMoimSchedule", sch_moimcode);
	}
	
		// 월간 일정 조회
	public List<Schedule> selectScheduleMonth(Map<String, Object> map) {
		return sqlSession.selectList(NSPACE + "selectScheduleMonth", map);
	}
	
		// 특정 일정 조회
	public Schedule selectSchedule(int sch_schnum) {
		return sqlSession.selectOne(NSPACE + "selectSchedule", sch_schnum);
	}
	
	
/* 일정 멤버 */
	// 일정 멤버 입력
	public int insertSchemem(Schemem schemem) {
		return sqlSession.insert(NSPACE + "insertSchemem", schemem);
	}
	
	// 일정 멤버 수정
		// 일정 참가자 내용 수정
	public int updateSchemem(Schemem schemem) {
		return sqlSession.update(NSPACE + "updateSchemem", schemem);
	}
	
		// 회비 액수 변경
	public int updateAmount(Schemem schemem) {
		return sqlSession.update(NSPACE + "updateAmount", schemem);
	}
	
		// 할 일 변경
	public int updateToDo(Schemem schemem) {
		return sqlSession.update(NSPACE + "updateToDo", schemem);
	}
	
		// 사용자 회비 납부 여부 갱신
	public int updateIsPay(Schemem schemem) {
		return sqlSession.update(NSPACE + "updateIsPay", schemem);
	}
	
		// 사용자 수행여부 갱신
	public int updateIsDo(Schemem schemem) {
		return sqlSession.update(NSPACE + "updateIsDo", schemem);
	}
	
	// 삭제
		// 일정 멤버 삭제(사용자 불참)
	public int deleteSchemem(Schemem schemem) {
		return sqlSession.delete(NSPACE + "deleteSchemem", schemem);
	}
	
		// 일정 삭제
	public int deleteSchememSche(int schnum) {
		return sqlSession.delete(NSPACE + "deleteSchememSche", schnum);
	}
	
		// 멤버 탈퇴
	public int deleteUserSchemem(String id) {
		return sqlSession.delete(NSPACE + "deleteUserSchemem", id);
	}
	
	// 조회
		// 일정별 전체 참가자 데이터
	public List<Schemem> selectSchemem(int schnum) {
		return sqlSession.selectList(NSPACE + "selectSchemem", schnum);
	}
	
		// 특정 일정내 특정 참가자 정보 조회
	public Schemem selectSchememUser(Schemem schemem) {
		return sqlSession.selectOne(NSPACE + "selectSchememUser", schemem);
	}
	
	
/* 마지막 모임 테이블명 조회 */	
	public String selectLastTname() {
		return sqlSession.selectOne(NSPACE + "selectLastTname");
	}
	
	
/* 모임 이름 테이블 */	
	// 입력
	public int insertTname(MoimTname moimTname) {
		return sqlSession.insert(NSPACE + "insertTname", moimTname);
	}
	
	// 삭제
	public int deleteTname(int moimcode) {
		return sqlSession.delete(NSPACE + "deleteTname", moimcode);
	}
	
	// 모임 테이블명 조회
	public String selectTname(int moimcode) {
		return sqlSession.selectOne(NSPACE + "selectTname", moimcode);
	}
	
	
/* 모임 테이블 */
	// 테이블 생성
	public void createMoim(String tname) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tname", tname);
		sqlSession.insert(NSPACE + "createMoim", map);
	}
	
	// 테이블 삭제
	public void dropMoim(String tname) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tname", tname);
		sqlSession.delete(NSPACE + "dropMoim", map);
	}
	
	// 테이블 존재 확인
	public int selectTnameCount(String tname) {
		return sqlSession.selectOne(NSPACE + "selectTnameCount", tname);
	}
	
	
/* 모임 */	
	// 모임 정보 입력
	public int insertMoim(Map<String, Object> map) {
		return sqlSession.insert(NSPACE + "insertMoim", map);
	}
	
	// 모임 정보 수정
	public int updateMoim(Map<String, Object> map) {
		return sqlSession.insert(NSPACE + "updateMoim", map);
	}
	
	// 모임코드 조회
	public int selectMoimCode(String tname) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tname", tname);
		return sqlSession.selectOne(NSPACE + "selectMoimCode", map);
	}
	
	// 모임 배너 파일명 조회
	public String selectMoimPic(String tname) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tname", tname);
		return sqlSession.selectOne(NSPACE + "selectMoimPic", map);
	}
	
	// 모임 정보 조회
	public Moim selectMoim(String tname) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tname", tname);
		return sqlSession.selectOne(NSPACE + "selectMoim", map);
	}
	
	// 모임 정보 및 가입 인원 수
	public Moim selectMoimNCount(String tname) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tname", tname);
		return sqlSession.selectOne(NSPACE + "selectMoimNCount", map);
	}
}