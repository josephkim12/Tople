package project.moim.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import project.moim.dto.BoardList;
import project.moim.dto.Moim;
import project.moim.dto.MoimMem;
import project.moim.dto.MoimUser;
import project.moim.dto.ReDat;
import project.moim.dto.Schedule;
import project.moim.dto.Schemem;
import project.moim.helper.FileHelper;
import project.moim.helper.ImgHelper;
import project.moim.service.MoimService;

@Controller
public class MoimController {

	final String base_path = "E:\\Android_ksh\\spring\\workspace_tmprojct\\moim.4t.spring_t4\\src\\main\\webapp\\storage";
	
	private @Autowired ServletContext context;
	
	private @Autowired MoimService service;

	// id로 사용자 정보 조회
	@ResponseBody
	@RequestMapping(value = "testID.tople", produces = "text/plain;charset=UTF-8")
	public String userInfo(HttpServletRequest request) {
		String id = request.getParameter("id");
		
		return service.selectUser(id);
	}
	
	// id, moimcode로 사용자 정보 조회
	@ResponseBody
	@RequestMapping(value = "selectUserMoimMem.tople", produces = "text/plain;charset=UTF-8")
	public String selectUserMoimMem(HttpServletRequest request) {
		String id = request.getParameter("id");
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		
		MoimMem moimMem = new MoimMem();
		moimMem.setId(id);
		moimMem.setMoimcode(moimcode);

		return service.selectUserMoimMem(moimMem);
	}
	
	// 회원 탈퇴
	@ResponseBody
	@RequestMapping(value = "blackUser.tople", produces = "text/plain;charset=UTF-8")
	public String blackUser(HttpServletRequest request) {
		String id = request.getParameter("id");

		return service.blackUser(id);
	}
	
	// 모임 멤버 가입
	@ResponseBody
	@RequestMapping(value = "insertMem.tople", produces = "text/plain;charset=UTF-8")
	public String insertMem(HttpServletRequest request) {
		String id = request.getParameter("id");
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		
		MoimMem moimMem = new MoimMem();
		moimMem.setId(id);
		moimMem.setMoimcode(moimcode);
		moimMem.setFav("false");
		moimMem.setPermit(3);

		return service.insertMem(moimMem);
	}
	
	// 모임 멤버 정보 수정
	@ResponseBody
	@RequestMapping(value = "updateMem.tople", produces = "text/plain;charset=UTF-8")
	public String updateMem(HttpServletRequest request) {
		String id = request.getParameter("id");
		String fav = request.getParameter("fav");
		int permit = Integer.parseInt(request.getParameter("permit"));
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		
		MoimMem moimMem = new MoimMem();
		moimMem.setId(id);
		moimMem.setFav(fav);
		moimMem.setPermit(permit);
		moimMem.setMoimcode(moimcode);

		return service.updateMem(moimMem);
	}
	
	// 모임 멤버 즐찾 수정
	@ResponseBody
	@RequestMapping(value = "updateMemUserFav.tople", produces = "text/plain;charset=UTF-8")
	public String updateMemUserFav(HttpServletRequest request) {
		String id = request.getParameter("id");
		String fav = request.getParameter("fav");
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		
		MoimMem moimMem = new MoimMem();
		moimMem.setId(id);
		moimMem.setFav(fav);
		moimMem.setMoimcode(moimcode);

		return service.updateMemUserFav(moimMem);
	}
	
	// 모임 멤버 권한 수정
	@ResponseBody
	@RequestMapping(value = "updateMemUserPermit.tople", produces = "text/plain;charset=UTF-8")
	public String updateMemUserPermit(HttpServletRequest request) {
		String id = request.getParameter("id");
		int permit = Integer.parseInt(request.getParameter("permit"));
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		
		MoimMem moimMem = new MoimMem();
		moimMem.setId(id);
		moimMem.setPermit(permit);
		moimMem.setMoimcode(moimcode);

		return service.updateMemUserPermit(moimMem);
	}
	
	// 모임코드로 사용자 정보 리스트 조회
	@ResponseBody
	@RequestMapping(value = "testMoimUsets.tople", produces = "text/plain;charset=UTF-8")
	public String selectMoimUserList(HttpServletRequest request) {
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		
		String result = service.selectMoimUsersInfo(moimcode);
		
		return result;
	}
	
	// 모임 생성
	@ResponseBody
	@RequestMapping(value = "testCreateMoim.tople", produces = "text/plain;charset=UTF-8")
	public String createMoim(MultipartHttpServletRequest request) {
		String id = request.getParameter("id");
		String loca = request.getParameter("loca");
		String moimname = request.getParameter("moimname");
		String prod = request.getParameter("prod");
		String color = request.getParameter("color");
		String fav = "true";
		int permit = 1;
		String pic = null;
		
		if (request.getFile("pic") != null) {
			MultipartFile file = request.getFile("pic");
			
			File banner = FileHelper.getInstance()
						  .fileCopyToPath(file, base_path, "/moimImg", "tople_");
			
			String metapath = context.getRealPath("/storage");
			FileHelper.getInstance().copyMeta(banner, metapath, "/moimImg");
			
			pic = banner.getName();
		} else {
			pic = "";
		}
		
		Moim moim = new Moim();
		moim.setColor(color);
		moim.setFav(fav);
		moim.setLoca(loca);
		moim.setMoimname(moimname);
		moim.setPermit(permit);
		moim.setProd(prod);
		moim.setPic(pic);
		
		String result = service.createMoim(moim, id);
		
		return result;
	}
	
	
	// 모임 정보 수정
	@ResponseBody
	@RequestMapping(value = "testUpdateMoim.tople", produces = "text/plain;charset=UTF-8")
	public String updateMoim(MultipartHttpServletRequest request) {
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		String loca = request.getParameter("loca");
		String moimname = request.getParameter("moimname");
		String prod = request.getParameter("prod");
		String color = request.getParameter("color");
		String pic = null;
		
		if (request.getFile("pic") != null) {
			MultipartFile file = request.getFile("pic");
			
			File banner = FileHelper.getInstance()
						  .fileCopyToPath(file, base_path, "/moimImg", "tople_");
			
			String metapath = context.getRealPath("/storage");
			FileHelper.getInstance().copyMeta(banner, metapath, "/moimImg");
			
			pic = banner.getName();
		} else {
			pic = "";
		}
		
		Moim moim = new Moim();
		moim.setMoimcode(moimcode);
		moim.setColor(color);
		moim.setLoca(loca);
		moim.setMoimname(moimname);
		moim.setProd(prod);
		moim.setPic(pic);
		
		String url = "http://" + request.getServerName() + ":" 
				 + request.getServerPort() +  request.getContextPath()
				 + "/storage/moimImg/";
		
		String result = service.updateMoim(moim, url);
		
		return result;
	}
	
	// 모임 삭제
	@ResponseBody
	@RequestMapping(value = "deleteMoim.tople", produces = "text/plain;charset=UTF-8")
	public String deleteMoim(HttpServletRequest request) {
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		
		return service.deleteMoim(moimcode);
	}
	
	// 사용자 모임 탈퇴 또는 강퇴 : 모임 가입 인원이 1명인 경우 모임 삭제
	@ResponseBody
	@RequestMapping(value = "dropUser.tople", produces = "text/plain;charset=UTF-8")
	public String dropUser(HttpServletRequest request) {
		String id = request.getParameter("id");
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		
		MoimMem mem = new MoimMem();
		mem.setId(id);
		mem.setMoimcode(moimcode);
		
		return service.dropUser(mem);
	}
	
	// 사용자 ID로 가입된 모임 리스트 얻어오기
	@ResponseBody
	@RequestMapping(value = "testMoimItems.tople", produces = "text/plain;charset=UTF-8")
	public String moimItems(HttpServletRequest request) {
		String id = request.getParameter("id");
		String url = "http://" + request.getServerName() + ":" 
					 + request.getServerPort() +  request.getContextPath()
					 + "/storage/moimImg/";
		
		return service.seletctListFormId(id, url);
	}

	
	// 특정 모임 정보 조회
	@ResponseBody
	@RequestMapping(value = "testMoim.tople", produces = "text/plain;charset=UTF-8")
	public String moimInfo(HttpServletRequest request) {
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		String url = "http://" + request.getServerName() + ":" 
				 + request.getServerPort() +  request.getContextPath()
				 + "/storage/moimImg/";

		return service.selectMoim(moimcode, url);
	}
	
	// 파일을 포함한 게시글 작성
	@ResponseBody
	@RequestMapping(value = "boardWrite.tople", produces = "text/plain;charset=UTF-8")
	public String insertFileBoard(MultipartHttpServletRequest request) {
		String id = request.getParameter("id");
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		int lev = Integer.parseInt(request.getParameter("lev"));
		
		BoardList boardList = new BoardList();
		boardList.setId(id);
		boardList.setMoimcode(moimcode);
		boardList.setSubject(subject);
		boardList.setContent(content);
		boardList.setLev(lev);
		
		// 사진
		if (request.getFile("filename") != null) {
			MultipartFile file = request.getFile("filename");
			
			// 원본사진 저장
			File origin = FileHelper.getInstance()
					  .fileCopyToPath(file, base_path, "/boardImg/origin", id);
			
			String metapath = context.getRealPath("/storage");
			
			FileHelper.getInstance().copyMeta(origin, metapath, "/boardImg/origin");
			
			// 썸네일 저장
			File thumb = FileHelper.getInstance()
					  .fileCopyToPath(file, base_path, "/boardImg/thum", "THUMB_"+id);
			
			File reThum = ImgHelper.getInstance().makeThumb(thumb, 500, 330);
			
			FileHelper.getInstance().copyMeta(reThum, metapath, "/boardImg/thum");
			
			boardList.setFilename(origin.getName());
			boardList.setThumb(reThum.getName());
		} else {
			boardList.setFilename("");
			boardList.setThumb("");
		}

		return service.insertBoard(boardList);
	}
	
	// 게시글 수정
	@ResponseBody
	@RequestMapping(value = "boardupdate.tople", produces = "text/plain;charset=UTF-8")
	public String updateFileBoard(MultipartHttpServletRequest request) {
		int listnum = Integer.parseInt(request.getParameter("listnum"));
		String id = request.getParameter("id");
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		int lev = Integer.parseInt(request.getParameter("lev"));
		
		BoardList boardList = new BoardList();
		boardList.setListnum(listnum);
		boardList.setId(id);
		boardList.setMoimcode(moimcode);
		boardList.setSubject(subject);
		boardList.setContent(content);
		boardList.setLev(lev);
		
		// 사진
		if (request.getFile("filename") != null) {
			MultipartFile file = request.getFile("filename");
			
			// 원본사진 저장
			File origin = FileHelper.getInstance()
					  .fileCopyToPath(file, base_path, "/boardImg/origin", id);
			
			String metapath = context.getRealPath("/storage");
			
			FileHelper.getInstance().copyMeta(origin, metapath, "/boardImg/origin");
			
			// 썸네일 저장
			File thumb = FileHelper.getInstance()
					  .fileCopyToPath(file, base_path, "/boardImg/thum", "THUMB_"+id);
			
			File reThum = ImgHelper.getInstance().makeThumb(thumb, 500, 330);
			
			FileHelper.getInstance().copyMeta(reThum, metapath, "/boardImg/thum");
			
			boardList.setFilename(origin.getName());
			boardList.setThumb(reThum.getName());
		} else {
			boardList.setFilename("");
			boardList.setThumb("");
		}

		return service.updateBoard(boardList);
	}
	
	// 게시글 삭제
		// 특정 게시글 삭제
	@ResponseBody
	@RequestMapping(value = "deleteBoard.tople", produces = "text/plain;charset=UTF-8")
	public String deleteBoard(HttpServletRequest request) {
		int listnum = Integer.parseInt(request.getParameter("listnum"));
		
		return service.deleteBoard(listnum);
	}
	
		// 모임 글 삭제
	@ResponseBody
	@RequestMapping(value = "deleteBoardList.tople", produces = "text/plain;charset=UTF-8")
	public String deleteBoardList(HttpServletRequest request) {
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		
		return service.deleteBoardList(moimcode);
	}
	
		// 사용자 게시글 삭제
	@ResponseBody
	@RequestMapping(value = "deleteUserBoard.tople", produces = "text/plain;charset=UTF-8")
	public String deleteUserBoard(HttpServletRequest request) {
		String id = request.getParameter("id");
		
		return service.deleteUserBoard(id);
	}
	
		// 모임내 사용자 게시글 삭제
	@ResponseBody
	@RequestMapping(value = "deleteMoimUserBoard.tople", produces = "text/plain;charset=UTF-8")
	public String deleteMoimUserBoard(HttpServletRequest request) {
		String id = request.getParameter("id");
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		
		MoimMem moimMem = new MoimMem();
		moimMem.setId(id);
		moimMem.setMoimcode(moimcode);
		
		return service.deleteMoimUserBoard(moimMem);
	}
	
	// 게시글 조회
		// 모임 내 필독 글 조회
	@ResponseBody
	@RequestMapping(value = "selectBoardFeel.tople", produces = "text/plain;charset=UTF-8")
	public String selectBoardFeel(HttpServletRequest request) {
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		String url = "http://" + request.getServerName() + ":" 
				 + request.getServerPort() +  request.getContextPath()
				 + "/storage/boardImg/";

		return service.selectBoardFeel(moimcode, url);
	}
	
		// 모임 내 일반 글 조회
	@ResponseBody
	@RequestMapping(value = "selectBoardNomal.tople", produces = "text/plain;charset=UTF-8")
	public String selectBoardNomal(HttpServletRequest request) {
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		String url = "http://" + request.getServerName() + ":" 
				 + request.getServerPort() +  request.getContextPath()
				 + "/storage/boardImg/";

		return service.selectBoardNomal(moimcode, url);
	}
	
		// 모임 내 전체 글 조회
	@ResponseBody
	@RequestMapping(value = "testselectMoimBoard.tople", produces = "text/plain;charset=UTF-8")
	public String selectMoimBoard(HttpServletRequest request) {
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		String url = "http://" + request.getServerName() + ":" 
				 + request.getServerPort() +  request.getContextPath()
				 + "/storage/boardImg/";

		return service.selectMoimBoard(moimcode, url);
	}
	
		// 특정 글 조회
	@ResponseBody
	@RequestMapping(value = "testselectBoard.tople", produces = "text/plain;charset=UTF-8")
	public String selectBoard(HttpServletRequest request) {
		int listnum = Integer.parseInt(request.getParameter("listnum"));
		String url = "http://" + request.getServerName() + ":" 
				 + request.getServerPort() +  request.getContextPath()
				 + "/storage/boardImg/";

		return service.selectBoard(listnum, url);
	}
	
	
	// 댓글 작성
	@ResponseBody
	@RequestMapping(value = "writeReDat.tople", produces = "text/plain;charset=UTF-8")
	public String insertReDat(HttpServletRequest request) {
		int listnum = Integer.parseInt(request.getParameter("listnum"));
		String id = request.getParameter("id");
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		String reple = request.getParameter("reple");
		
		ReDat reDat = new ReDat();
		reDat.setListnum(listnum);
		reDat.setId(id);
		reDat.setMoimcode(moimcode);
		reDat.setReple(reple);
		
		return service.insertReDat(reDat);
	}
	
	// 덧 글 수정
	@ResponseBody
	@RequestMapping(value = "updateReDat.tople", produces = "text/plain;charset=UTF-8")
	public String updateReDat(HttpServletRequest request) {
		int renum = Integer.parseInt(request.getParameter("renum"));
		int listnum = Integer.parseInt(request.getParameter("listnum"));
		String id = request.getParameter("id");
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		String reple = request.getParameter("reple");
		
		ReDat reDat = new ReDat();
		reDat.setRenum(renum);
		reDat.setListnum(listnum);
		reDat.setId(id);
		reDat.setMoimcode(moimcode);
		reDat.setReple(reple);
		
		return service.updateReDat(reDat);
	}
	
	// 덧 글 삭제
		// 특정 덧 글 삭제
	@ResponseBody
	@RequestMapping(value = "deleteRedat.tople", produces = "text/plain;charset=UTF-8")
	public String deleteRedat(HttpServletRequest request) {
		int renum = Integer.parseInt(request.getParameter("renum"));

		return service.deleteRedat(renum);
	}
	
		// 원 글에 종속된 모든 덧 글 삭제
	@ResponseBody
	@RequestMapping(value = "deleteBoardReDat.tople", produces = "text/plain;charset=UTF-8")
	public String deleteBoardReDat(HttpServletRequest request) {
		int listnum = Integer.parseInt(request.getParameter("listnum"));

		return service.deleteBoardReDat(listnum);
	}
	
		// 모임내 덧 글 삭제
	@ResponseBody
	@RequestMapping(value = "deleteMoimRedat.tople", produces = "text/plain;charset=UTF-8")
	public String deleteMoimRedat(HttpServletRequest request) {
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));

		return service.deleteMoimRedat(moimcode);
	}
	
		// 사용자 작성 덧 글 삭제
	@ResponseBody
	@RequestMapping(value = "deleteUserRedat.tople", produces = "text/plain;charset=UTF-8")
	public String deleteUserRedat(HttpServletRequest request) {
		String id = request.getParameter("id");

		return service.deleteUserRedat(id);
	}
	
		// 모임내 사용자 작성 덧 글 삭제
	@ResponseBody
	@RequestMapping(value = "deleteMoimUserRedat.tople", produces = "text/plain;charset=UTF-8")
	public String deleteMoimUserRedat(HttpServletRequest request) {
		String id = request.getParameter("id");
		int moimcode = Integer.parseInt(request.getParameter("moimcode"));
		
		MoimMem moimMem = new MoimMem();
		moimMem.setId(id);
		moimMem.setMoimcode(moimcode);

		return service.deleteMoimUserRedat(moimMem);
	}
	
	// 덧 글 조회
		// 원 글에 종속된 덧글 전체 조회
	@ResponseBody
	@RequestMapping(value = "selectReDatList.tople", produces = "text/plain;charset=UTF-8")
	public String selectReDatList(HttpServletRequest request) {
		int listnum = Integer.parseInt(request.getParameter("listnum"));
		
		return service.selectReDatList(listnum);
	}
	
		// 특정 덧 글 조회
	@ResponseBody
	@RequestMapping(value = "selectRedat.tople", produces = "text/plain;charset=UTF-8")
	public String selectRedat(HttpServletRequest request) {
		int renum = Integer.parseInt(request.getParameter("renum"));
		
		return service.selectReDat(renum);
	}
	
	
/* 일정 */	
	// 일정 등록
	@ResponseBody
	@RequestMapping(value = "insertSchedule.tople", produces = "text/plain;charset=UTF-8")
	public String insertSchedule(HttpServletRequest request) {
		int sch_moimcode = Integer.parseInt(request.getParameter("sch_moimcode"));
		String sch_year = request.getParameter("sch_year");
		String sch_month = request.getParameter("sch_month");
		String sch_day = request.getParameter("sch_day");
		String sch_title = request.getParameter("sch_title");
		String sch_time = request.getParameter("sch_time");
		String sch_sub = request.getParameter("sch_sub");
		String sch_amount = request.getParameter("sch_amount");
		double sch_lat;
		double sch_lot;
		
		if (request.getParameter("sch_lat") != null) {
			sch_lat = Double.parseDouble(request.getParameter("sch_lat"));
		} else {
			sch_lat = 0.0;
		}
		
		if (request.getParameter("sch_lot") != null) {
			sch_lot = Double.parseDouble(request.getParameter("sch_lot"));
		} else {
			sch_lot = 0.0;
		}
		
		Schedule schedule = new Schedule();
		schedule.setSch_moimcode(sch_moimcode);
		schedule.setSch_year(sch_year);
		schedule.setSch_month(sch_month);
		schedule.setSch_day(sch_day);
		schedule.setSch_title(sch_title);
		schedule.setSch_time(sch_time);
		schedule.setSch_sub(sch_sub);
		schedule.setSch_amount(sch_amount);
		schedule.setSch_lat(sch_lat);
		schedule.setSch_lot(sch_lot);
		
		return service.insertSchedule(schedule);
	}
	
	
	// 일정 수정
	@ResponseBody
	@RequestMapping(value = "updateSchedule.tople", produces = "text/plain;charset=UTF-8")
	public String updateSchedule(HttpServletRequest request) {
		int sch_moimcode = Integer.parseInt(request.getParameter("sch_moimcode"));
		int sch_schnum = Integer.parseInt(request.getParameter("sch_schnum"));
		String sch_year = request.getParameter("sch_year");
		String sch_month = request.getParameter("sch_month");
		String sch_day = request.getParameter("sch_day");
		String sch_title = request.getParameter("sch_title");
		String sch_time = request.getParameter("sch_time");
		String sch_sub = request.getParameter("sch_sub");
		String sch_amount = request.getParameter("sch_amount");
		double sch_lat;
		double sch_lot;
		
		if (request.getParameter("sch_lat") != null) {
			sch_lat = Double.parseDouble(request.getParameter("sch_lat"));
		} else {
			sch_lat = 0.0;
		}
		
		if (request.getParameter("sch_lot") != null) {
			sch_lot = Double.parseDouble(request.getParameter("sch_lot"));
		} else {
			sch_lot = 0.0;
		}
		
		Schedule schedule = new Schedule();
		schedule.setSch_moimcode(sch_moimcode);
		schedule.setSch_schnum(sch_schnum);
		schedule.setSch_year(sch_year);
		schedule.setSch_month(sch_month);
		schedule.setSch_day(sch_day);
		schedule.setSch_title(sch_title);
		schedule.setSch_time(sch_time);
		schedule.setSch_sub(sch_sub);
		schedule.setSch_amount(sch_amount);
		schedule.setSch_lat(sch_lat);
		schedule.setSch_lot(sch_lot);
		
		return service.updateSchedule(schedule);
	}
	
	// 일정 삭제
		// 모임 일정 전체 삭제
	@ResponseBody
	@RequestMapping(value = "deleteMoimSchedule.tople", produces = "text/plain;charset=UTF-8")
	public String deleteMoimSchedule(HttpServletRequest request) {
		int sch_moimcode = Integer.parseInt(request.getParameter("sch_moimcode"));
		
		return service.deleteMoimSchedule(sch_moimcode);
	}
	
		// 단일 일정 삭제
	@ResponseBody
	@RequestMapping(value = "deleteSchedule.tople", produces = "text/plain;charset=UTF-8")
	public String deleteSchedule(HttpServletRequest request) {
		int sch_schnum = Integer.parseInt(request.getParameter("sch_schnum"));
		
		return service.deleteSchedule(sch_schnum);
	}
	
	// 일정 조회
		// 일정 전체 조회
	@ResponseBody
	@RequestMapping(value = "selectMoimSchedule.tople", produces = "text/plain;charset=UTF-8")
	public String selectMoimSchedule(HttpServletRequest request) {
		int sch_moimcode = Integer.parseInt(request.getParameter("sch_moimcode"));
		
		return service.selectMoimSchedule(sch_moimcode);
	}
	
		// 월간 일정 조회
	@ResponseBody
	@RequestMapping(value = "selectScheduleMonth.tople", produces = "text/plain;charset=UTF-8")
	public String selectScheduleMonth(HttpServletRequest request) {
		int sch_moimcode = Integer.parseInt(request.getParameter("sch_moimcode"));
		String sch_year = request.getParameter("sch_year");
		String sch_month = request.getParameter("sch_month");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sch_moimcode", sch_moimcode);
		map.put("sch_year", sch_year);
		map.put("sch_month", sch_month);
		
		return service.selectScheduleMonth(map);
	}
	
		// 일정 상세 조회
	@ResponseBody
	@RequestMapping(value = "selectSchedule.tople", produces = "text/plain;charset=UTF-8")
	public String selectSchedule(HttpServletRequest request) {
		int sch_schnum = Integer.parseInt(request.getParameter("sch_schnum"));
		
		return service.selectSchedule(sch_schnum);
	}
	
	
/* 일정 멤버 */
	// 입력
	@ResponseBody
	@RequestMapping(value = "insertSchemem.tople", produces = "text/plain;charset=UTF-8")
	public String insertSchemem(HttpServletRequest request) {
		int schnum = Integer.parseInt(request.getParameter("sch_schnum"));
		String id = request.getParameter("id");
		
		Schemem schemem = new Schemem();
		schemem.setSchnum(schnum);
		schemem.setId(id);
		
		if (request.getParameter("todo") != null) {
			String todo = request.getParameter("todo");
			schemem.setTodo(todo);
		} else {
			schemem.setTodo("");
		}
		
		if (request.getParameter("ex") != null) {
			String ex = request.getParameter("ex");
			schemem.setEx(ex);
		} else {
			schemem.setEx("");
		}
		
		if (request.getParameter("isdo") != null) {
			String isdo = request.getParameter("isdo");
			schemem.setIsdo(isdo);
		} else {
			schemem.setIsdo("false");
		}
		
		if (request.getParameter("amount") != null) {
			String amount = request.getParameter("amount");
			schemem.setAmount(amount);
		} else {
			schemem.setAmount("");
		}
		
		if (request.getParameter("ispay") != null) {
			String ispay = request.getParameter("ispay");
			schemem.setIspay(ispay);
		} else {
			schemem.setIspay("false");
		}
		
		return service.insertSchemem(schemem);
	}
	
	// 수정
		// 일정 참가자 내용 일괄 변경
	@ResponseBody
	@RequestMapping(value = "updateSchemem.tople", produces = "text/plain;charset=UTF-8")
	public String updateSchemem(HttpServletRequest request) {
		int schnum = Integer.parseInt(request.getParameter("sch_schnum"));
		String id = request.getParameter("id");
		
		Schemem schemem = new Schemem();
		schemem.setSchnum(schnum);
		schemem.setId(id);
		
		if (request.getParameter("todo") != null) {
			String todo = request.getParameter("todo");
			schemem.setTodo(todo);
		} else {
			schemem.setTodo("");
		}
		
		if (request.getParameter("ex") != null) {
			String ex = request.getParameter("ex");
			schemem.setEx(ex);
		} else {
			schemem.setEx("");
		}
		
		if (request.getParameter("isdo") != null) {
			String isdo = request.getParameter("isdo");
			schemem.setIsdo(isdo);
		} else {
			schemem.setIsdo("");
		}
		
		if (request.getParameter("amount") != null) {
			String amount = request.getParameter("amount");
			schemem.setAmount(amount);
		} else {
			schemem.setAmount("");
		}
		
		if (request.getParameter("ispay") != null) {
			String ispay = request.getParameter("ispay");
			schemem.setIspay(ispay);
		} else {
			schemem.setIspay("");
		}
		
		return service.updateSchemem(schemem);
	}
	
		// 회비 액수 변경
	@ResponseBody
	@RequestMapping(value = "updateAmount.tople", produces = "text/plain;charset=UTF-8")
	public String updateAmount(HttpServletRequest request) {
		int schnum = Integer.parseInt(request.getParameter("sch_schnum"));
		String id = request.getParameter("id");
		String amount = request.getParameter("amount");
		
		Schemem schemem = new Schemem();
		schemem.setSchnum(schnum);
		schemem.setId(id);
		schemem.setAmount(amount);
		
		return service.updateAmount(schemem);
	}
	
		// 할 일 변경
	@ResponseBody
	@RequestMapping(value = "updateToDo.tople", produces = "text/plain;charset=UTF-8")
	public String updateToDo(HttpServletRequest request) {
		int schnum = Integer.parseInt(request.getParameter("sch_schnum"));
		String id = request.getParameter("id");
		String todo = request.getParameter("todo");
		String ex = request.getParameter("ex");
		
		Schemem schemem = new Schemem();
		schemem.setSchnum(schnum);
		schemem.setId(id);
		schemem.setTodo(todo);
		schemem.setEx(ex);
		
		return service.updateToDo(schemem);
	}
	
		// 사용자 수행여부 갱신
	@ResponseBody
	@RequestMapping(value = "updateIsDo.tople", produces = "text/plain;charset=UTF-8")
	public String updateIsDo(HttpServletRequest request) {
		int schnum = Integer.parseInt(request.getParameter("sch_schnum"));
		String id = request.getParameter("id");
		String isdo = request.getParameter("isdo");
		
		Schemem schemem = new Schemem();
		schemem.setSchnum(schnum);
		schemem.setId(id);
		schemem.setIsdo(isdo);
		
		return service.updateIsDo(schemem);
	}
	
		// 사용자 회비 납부 여부 갱신
	@ResponseBody
	@RequestMapping(value = "updateIsPay.tople", produces = "text/plain;charset=UTF-8")
	public String updateIsPay(HttpServletRequest request) {
		int schnum = Integer.parseInt(request.getParameter("sch_schnum"));
		String id = request.getParameter("id");
		String ispay = request.getParameter("ispay");
		
		Schemem schemem = new Schemem();
		schemem.setSchnum(schnum);
		schemem.setId(id);
		schemem.setIspay(ispay);
		
		return service.updateIsPay(schemem);
	}
	
	
	// 삭제
		// 일정 멤버 삭제
	@ResponseBody
	@RequestMapping(value = "deleteSchemem.tople", produces = "text/plain;charset=UTF-8")
	public String deleteSchemem(HttpServletRequest request) {
		int schnum = Integer.parseInt(request.getParameter("sch_schnum"));
		String id = request.getParameter("id");
		
		Schemem schemem = new Schemem();
		schemem.setSchnum(schnum);
		schemem.setId(id);
		
		return service.deleteSchemem(schemem);
	}
	
		// 멤버 탈퇴
	@ResponseBody
	@RequestMapping(value = "deleteUserSchemem.tople", produces = "text/plain;charset=UTF-8")
	public String deleteUserSchemem(HttpServletRequest request) {
		String id = request.getParameter("id");
		
		return service.deleteUserSchemem(id);
	}
	
	// 조회
		// 일정 별 참가자 데이터 조회
	@ResponseBody
	@RequestMapping(value = "selectSchemem.tople", produces = "text/plain;charset=UTF-8")
	public String selectSchemem(HttpServletRequest request) {
		int schnum = Integer.parseInt(request.getParameter("sch_schnum"));
		
		return service.selectSchemem(schnum);
	}
	
	// 사용자 중복 검사
	@ResponseBody
	@RequestMapping(value = "isExistUser.tople", produces = "text/plain;charset=UTF-8")
	public String isExistUser(HttpServletRequest request) {
		String id = request.getParameter("id");
		
		return service.isExistUser(id);
	}
	
	// 사용자 정보 입력
	@ResponseBody
	@RequestMapping(value = "insertUser.tople", produces = "text/plain;charset=UTF-8")
	public String insertUser(HttpServletRequest request) {
		String id = request.getParameter("id");
		
		MoimUser moimUser = new MoimUser();
		moimUser.setId(id);
		moimUser.setUser_img("");
		
		return service.insertUser(moimUser);
	}
	
	// 사용자 정보 수정
	@ResponseBody
	@RequestMapping(value = "userProf.tople", produces = "text/plain;charset=UTF-8")
	public String test(MultipartHttpServletRequest request) {
		String url = "http://" + request.getServerName() + ":" 
				+ request.getServerPort() +  request.getContextPath()
				+ "/storage";
		
		String subpath = "/userImg";
		
		String filename = "";
		
		String id = request.getParameter("id");
		
		if (request.getFile("prof") != null) {
			MultipartFile file = request.getFile("prof");
			
			File prof = FileHelper.getInstance()
						  .fileCopyToPath(file, base_path, "/userImg", id);
			
			String metapath = context.getRealPath("/storage");
			
			FileHelper.getInstance().copyMeta(prof, metapath, "/userImg");
			
			// 썸네일 저장
			File thumb = FileHelper.getInstance()
					  .fileCopyToPath(file, base_path, "/userImg/thum", "THUMB_"+id);
			
			File reThum = ImgHelper.getInstance().makeThumb(thumb, 500, 330);
			
			FileHelper.getInstance().copyMeta(reThum, metapath, "/userImg/thum");
			
			filename = prof.getName();
		}
		
		MoimUser moimUser = new MoimUser();
		moimUser.setId(id);
		moimUser.setUser_img(filename);
		
		service.updateUser(moimUser);
		
		return url + subpath + "/" +filename;
	}
	
	/**	<<테스트>>
	 * http://192.168.0.93:8080/moim.4t.spring/test.tople post 방식으로 카카오 accesstoken을
	 * 포함하여 호출하면 REST API 방식으로 카카오서버로부터 사용자 정보를 조회한다.
	 * 
	 * @RequestMapping(value="test.tople")
	 * 
	 * @ResponseBody public String userInfo(HttpServletRequest request) { String url
	 * = "https://kapi.kakao.com/v2/user/me"; String token =
	 * request.getParameter("access_token");
	 * 
	 * System.out.println(token);
	 * 
	 * HttpHeaders headers = new HttpHeaders();
	 * headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	 * headers.set("Authorization", "Bearer " + token);
	 * 
	 * HttpEntity<String> entity = new HttpEntity<String>("Headers", headers);
	 * 
	 * System.out.println(entity.toString());
	 * 
	 * ResponseEntity<String> response = restTemplate.postForEntity(url, entity,
	 * String.class);
	 * 
	 * System.out.println(response.getBody());
	 * 
	 * return response.getBody(); }
	 **/
}