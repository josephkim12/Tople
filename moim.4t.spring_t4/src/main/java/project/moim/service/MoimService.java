package project.moim.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import project.moim.dao.MoimUserDAO;
import project.moim.dto.BoardList;
import project.moim.dto.Moim;
import project.moim.dto.MoimMem;
import project.moim.dto.MoimTname;
import project.moim.dto.MoimUser;
import project.moim.dto.ReDat;
import project.moim.dto.Schedule;
import project.moim.dto.Schemem;

@Service
public class MoimService {
	final String base_path = "E:\\Android_ksh\\spring\\workspace_tmprojct\\moim.4t.spring_t4\\src\\main\\webapp\\storage";
	private final String app_key = "54002ac0d722d2da02e369fe41470e75";

	private @Autowired MoimUserDAO dao;
	
	RestTemplate restTemplate;
	
	public MoimService() {
		restInit();
	}
	
	private void restInit() {
		HttpComponentsClientHttpRequestFactory factory 
			= new HttpComponentsClientHttpRequestFactory(); 
		
		factory.setReadTimeout(5000);
		factory.setConnectTimeout(3000);
		
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setMaxConnTotal(100);
		builder.setMaxConnPerRoute(5);
		
		HttpClient client = builder.build();
		
		factory.setHttpClient(client);
		
		restTemplate = new RestTemplate(factory);
	}
	
	// 단일 사용자 정보 요청
	public String selectUser(String id) {
		final String url = "https://kapi.kakao.com/v2/user/me";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("application/x-www-form-urlencoded;charset=utf-8"));
		headers.set("Authorization", "KakaoAK " + app_key);
		
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("target_id_type", "user_id");
		map.add("target_id", id);
		
		HttpEntity<MultiValueMap<String, Object>> entity 
			= new HttpEntity<MultiValueMap<String,Object>>(map, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

		return response.getBody();
	}
	
	// 사용자 앱 연결 해제 : 카카오 연결 해제
	public String unlinkUser(String id) {
		final String url = "https://kapi.kakao.com/v1/user/unlink";
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + app_key);
		
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("target_id_type", "user_id");
		map.add("target_id", id);
		
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String,Object>>(map, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

		return response.getBody();
	}
	
	// 사용자 닉네임 조회
	private String getName(String id) {
		String name;
		
		JSONObject json = new JSONObject(selectUser(id));
		JSONObject proper = json.getJSONObject("properties");
		
		name = proper.getString("nickname");
		
		return name;
	}
	
	// 사용자 id 중복 검사
	public String isExistUser(String id) {
		String result = "EXIST";
		
		JSONObject json = new JSONObject();
		
		if (dao.selectUser(id) == null) {
			result = "NOTEXIST";
		}
		
		json.put("result", result);
		
		return json.toString();
	}
	
	// 다중 사용자 정보 요청
	public String selectMoimUsersInfo(int moimcode) {
		List<MoimMem> members = dao.selectMoimMem(moimcode);
		
		JSONObject json = new JSONObject();
		
		List<JSONObject> users = new ArrayList<JSONObject>();
		
		for (MoimMem moimMem : members) {
			JSONObject user = new JSONObject(selectUser(moimMem.getId()));
			
			user.put("fav", moimMem.getFav());
			user.put("permit", moimMem.getPermit());
			
			users.add(user);
		}
		
		json.put("users", users);
		
		return json.toString();
	}
	
	
	// 사용자 특정 모임 정보 조회
	public String selectUserMoimMem(MoimMem moimMem) {
		MoimMem mem = dao.selectUserMoimMem(moimMem);
		
		JSONObject json = new JSONObject();
		
		JSONObject user = new JSONObject(selectUser(moimMem.getId()));
		
		if (mem != null) {
			user.put("fav", mem.getFav());
			user.put("permit", mem.getPermit());
		}
			
		json.put("item", user);
		
		return json.toString();
	}
	
	// 사용자 정보 저장
	public String insertUser(MoimUser moimUser) {
		String result = "FAIL";
		
		JSONObject temp = new JSONObject(isExistUser(moimUser.getId()));
		
		if (temp.getString("result").equals("NOTEXIST")) {
			if (dao.insertUser(moimUser) > 0) {
				result = "OK";
			}
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 사용자 프로필 수정
	public void updateUser(MoimUser moimUser) {
		String userOldImg = dao.selectUserProf(moimUser.getId());
		
		if (!moimUser.getUser_img().equals("")) {
			String filePath = base_path + "/userImg";
			
			if (userOldImg != null) {
				File oldProf = new File(filePath, userOldImg);
				if (oldProf.exists()) {
					oldProf.delete();
				}
				
				File oldThum = new File(filePath + "/thum", userOldImg);
				if (oldThum.exists()) {
					oldThum.delete();
				}
			}
		} else {
			moimUser.setUser_img(userOldImg);
		}
	}
	
	// 로그인시 모임리스트 요청
	public String seletctListFormId(String id, String url) {
		List<MoimMem> moimMemList = dao.selectUserMoimList(id);
		
		JSONObject json = new JSONObject();
		List<Moim> items = new ArrayList<Moim>();
		
		for (MoimMem moimMem : moimMemList) {
			String tname = dao.selectTname(moimMem.getMoimcode());
			Moim moim = dao.selectMoimNCount(tname);
			
			moim.setPic(url + moim.getPic());
			
			moim.setFav(moimMem.getFav());
			moim.setPermit(moimMem.getPermit());
			
			// 즐겨찾기 모임 우선 정렬
			if (Boolean.parseBoolean(moim.getFav())) {
				items.add(0, moim);
			} else {
				items.add(moim);
			}
		}
		
		json.put("items", items);
		
		return json.toString();
	}
	
	// 회원 탈퇴
	public String blackUser(String id) {
		String result = "FAIL";
		
		JSONObject json = new JSONObject();
		
		// 탈퇴한 사용자 프로필 삭제
		MoimUser moimUser = dao.selectUser(id);
		
		// 유저정보 삭제
		if (dao.deleteUser(id) > 0) {
			String filePath = base_path + "/userImg";
			
			if (moimUser != null) {
				String userImg = moimUser.getUser_img();
				
				if (userImg != null) {
					File oldProf = new File(filePath, userImg);
					if (oldProf.exists()) {
						oldProf.delete();
					}
					
					File oldThum = new File(filePath + "/thum", userImg);
					if (oldThum.exists()) {
						oldThum.delete();
					}
				}
			}
			
			// 탈퇴한 사용자가 가입한 모임 탈퇴
			List<MoimMem> memList = dao.selectUserMoimList(id);
			
			for (MoimMem moimMem : memList) {
				dropUser(moimMem);
			}
			
			// 참가 일정에서 참가 해제
			deleteUserSchemem(id);
			
			result = "OK";
		}
		
		json.put("result", result);
		
		return json.toString();
	}
	
	// 모임 멤버 가입
	public String insertMem(MoimMem moimMem) {
		String result;
		
		JSONObject json = new JSONObject();
		
		MoimMem old = dao.selectUserMoimMem(moimMem);
		
		if (old != null) {
			json.put("result", "ALREADY");
			return json.toString();
		}
		
		if (dao.insertMem(moimMem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		json.put("result", result);
		
		return json.toString();
	}
	
	// 모임멤버 정보 수정
	public String updateMem(MoimMem moimMem) {
		String result;
		
		if (dao.updateMem(moimMem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 모임 멤버 즐찾 수정
	public String updateMemUserFav(MoimMem moimMem) {
		String result;
		
		if (dao.updateMemUserFav(moimMem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 모임 멤버 권한 수정
	public String updateMemUserPermit(MoimMem moimMem) {
		String result;
		
		if (dao.updateMemUserPermit(moimMem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}

	// 모임 생성
	public String createMoim(Moim moim, String id) {
		JSONObject json = new JSONObject();
		
		String result = "";
		
		String exitLastTname = dao.selectLastTname();
		String temp = exitLastTname.substring(2);
		int serial = Integer.parseInt(temp);
		serial++;
		
		String tname = "zz" + serial;
		
		dao.createMoim(tname);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tname", tname);
		map.put("moim", moim);
		
		if (dao.insertMoim(map) > 0) {
			int moimcode = dao.selectMoimCode(tname);
			
			if (moimcode > 0) {
				MoimTname moimTname = new MoimTname();
				moimTname.setMoimcode(moimcode);
				moimTname.setTname(tname);
				
				if (dao.insertTname(moimTname) < 1) {
					result = "테이블명 + 모임코드 등록 실패";
				}
				
				MoimMem mem = new MoimMem();
				mem.setId(id);
				mem.setMoimcode(moimcode);
				mem.setPermit(moim.getPermit());
				mem.setFav(moim.getFav());
				
				if (dao.insertMem(mem) > 0) {
					JSONObject info = new JSONObject(dao.selectMoimNCount(tname));
					info.put("fav", moim.getFav());
					info.put("permit", moim.getPermit());
					
					result = "OK";
					
					json.put("item", info);
				} else {
					result = "모임장  모임멤버 등록 실패";
				}
			}		
		} else {
			result = "모임 정보 입력 실패";
		}
		
		json.put("result", result);
		
		return json.toString();
	}
	
	// 모임 정보 수정
	public String updateMoim(Moim moim, String url) {
		String result = "OK";
		
		String tname = dao.selectTname(moim.getMoimcode());
		String fileName = dao.selectMoimPic(tname);
		
		if (!moim.getPic().equals("")) {
			String filePath = base_path + "/moimImg";
			
			if (fileName != null) {
				File oldBanner = new File(filePath, fileName);
				if (oldBanner.exists()) {
					oldBanner.delete();
				}
			}
		} else {
			moim.setPic(fileName);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tname", tname);
		map.put("moim", moim);
		
		if (dao.updateMoim(map) < 1) {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject(selectMoim(moim.getMoimcode(), url));
		json.put("result", result);
		
		return json.toString();
	}
	
	
	// 사용자 퇴장(모임 탈퇴 또는 강퇴)
	public String dropUser(MoimMem mem) {
		String result = "FAIL";
		
		if (dao.selectMoimMem(mem.getMoimcode()).size() <= 1) {
			// 모임내 가입인원이 1명인 경우 모임 삭제
			deleteMoim(mem.getMoimcode());
			result = "OK";
		} else {
			// 모임멤버 테이블에서 사용자 삭제
			if (dao.deleteMem(mem) > 0) {
				result = "OK";
			} else {
				result = "FAIL";
			}
			
			
			// 모임내 사용자 게시글 조회
			List<BoardList> userboard = dao.selectMoimUserBoard(mem);
			
			if (userboard.size() > 0) {
				// 모임내 사용자 게시글 삭제
				dao.deleteMoimUserBoard(mem);
				
				// 모임내 스케줄 조회
				List<Schedule> schlist = dao.selectMoimSchedule(mem.getMoimcode());
				
				for (Schedule schedule : schlist) {
					Schemem schemem = new Schemem();
					schemem.setId(mem.getId());
					schemem.setSchnum(schedule.getSch_schnum());
					
					dao.deleteSchemem(schemem);
				}
				
				result = "OK";
			}
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 특정 모임 정보 요청
	public String selectMoim(int moimcode, String url) {
		String tname = dao.selectTname(moimcode);
		
		JSONObject object = new JSONObject();

		if (tname == null) {
			object.put("result", "FAIL");
		} else {
			Moim moim = dao.selectMoimNCount(tname);
			moim.setPic(url + moim.getPic());
			
			ArrayList<Moim> list = new ArrayList<Moim>();
			list.add(moim);
			
			object.put("result", "OK");
			object.put("items", list);
		}
		
		return object.toString();
	}
	
	// 모임 삭제
	public String deleteMoim(int moimcode) {
		String result = "FAIL";
		
		JSONObject json = new JSONObject();
		
		// 모임 테이블명 얻기
		String tname = dao.selectTname(moimcode);
		
		if (tname != null) {
			// 모임 테이블 삭제
			dao.dropMoim(tname);

			// 모임멤버에서 모임에 가입된 사용자 삭제
			dao.deleteMoimMem(moimcode);
			
			// 테이블명 삭제
			dao.deleteTname(moimcode);
			
			// 게시글 삭제
			dao.deleteBoardList(moimcode);
			
			// 일정 삭제
			dao.deleteMoimSchedule(moimcode);
			
			result = "OK";
		}
		
		json.put("result", result);
		
		return json.toString();
	}
	
	// 게시글 작성
	public String insertBoard(BoardList boardList) {
		String result;
		
		if (dao.insertBoard(boardList) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 게시글 수정
	public String updateBoard(BoardList boardList) {
		String result;
		
		BoardList old = dao.selectBoard(boardList.getListnum());
		
		String oldfilename = old.getFilename();
		String oldthumname = old.getThumb();
		
		String boardPath = base_path + "/boardImg";
		
		// 사진 수정 시 기존 사진 삭제
		if (!boardList.getFilename().equals("")) {
			if (oldfilename != null) {
				File oldfile = new File(boardPath + "/origin", oldfilename);
				
				if (oldfile.exists()) {
					oldfile.delete();
				}
			}
		} else {
			boardList.setFilename(oldfilename);
		}
		
		if (!boardList.getThumb().equals("")) {
			if (oldthumname != null) {
				File oldthumb = new File(boardPath + "/thum", oldthumname);
				
				if (oldthumb.exists()) {
					oldthumb.delete();
				}
			}
		} else {
			boardList.setThumb(oldthumname);
		}
		
		if (dao.updateBoard(boardList) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 게시글 삭제
		// 특정 게시글 삭제
	public String deleteBoard(int listnum) {
		String result;
		
		BoardList boardList = dao.selectBoard(listnum);
		
		// 사진 삭제
		if (boardList.getFilename() != null) {
			String originpath = base_path + "/boardImg/origin";
			String imgname = boardList.getFilename();
			
			File origin = new File(originpath, imgname);
			
			if (origin.exists()) {
				origin.delete();
			}
		}
		
		// 썸네일 삭제
		if (boardList.getThumb() != null) {
			String thumpath = base_path + "/boardImg/thum";
			String thumname = boardList.getThumb();
			
			File thumb = new File(thumpath, thumname);
			
			if (thumb.exists()) {
				thumb.delete();
			}
		}
		
		List<ReDat> reList = dao.selectReDatList(listnum);
		
		if (dao.deleteBoard(listnum) > 0) {
			for (ReDat reDat : reList) {
				deleteRedat(reDat.getRenum());
			}
			
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 모임 글 삭제
	public String deleteBoardList(int moimcode) {
		String result;
		
		List<BoardList> list = dao.selectMoimBoard(moimcode);
		
		if (list.size() > 0) {
			for (BoardList boardList : list) {
				deleteBoard(boardList.getListnum());
			}
			
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 사용자 게시글 삭제
	public String deleteUserBoard(String id) {
		String result;
		
		List<BoardList> list = dao.selectUserBoard(id);
		
		if (list.size() > 0) {
			for (BoardList boardList : list) {
				deleteBoard(boardList.getListnum());
			}
			
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 모임내 사용자 게시글 삭제
	public String deleteMoimUserBoard(MoimMem moimMem) {
		String result;
		
		List<BoardList> list = dao.selectMoimUserBoard(moimMem);
		
		if (list.size() > 0) {
			for (BoardList boardList : list) {
				deleteBoard(boardList.getListnum());
			}
			
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 필독 글 조회
	public String selectBoardFeel(int moimcode, String url) {
		String originurl = url + "/origin/";
		String thumurl = url + "/thum/";
		
		List<BoardList> list = dao.selectBoardFeel(moimcode);
		
		ArrayList<BoardList> feel = new ArrayList<BoardList>();

		for (BoardList boardList : list) {
			if (boardList.getFilename() != null) {
				boardList.setFilename(originurl + boardList.getFilename());
			} else {
				boardList.setFilename("");
			}
			if (boardList.getThumb() != null) {
				boardList.setThumb(thumurl + boardList.getThumb());
			} else {
				boardList.setThumb("");
			}
			
			boardList.setName(getName(boardList.getId()));
			
			feel.add(boardList);
		}
		
		JSONObject json = new JSONObject();
		json.put("feel", feel);
		
		return json.toString();
	}
	
	// 일반 글 조회
	public String selectBoardNomal(int moimcode, String url) {
		String originurl = url + "/origin/";
		String thumurl = url + "/thum/";
		
		List<BoardList> list = dao.selectBoardNomal(moimcode);
		
		ArrayList<BoardList> normal = new ArrayList<BoardList>();

		for (BoardList boardList : list) {
			if (boardList.getFilename() != null) {
				boardList.setFilename(originurl + boardList.getFilename());
			} else {
				boardList.setFilename("");
			}
			if (boardList.getThumb() != null) {
				boardList.setThumb(thumurl + boardList.getThumb());
			} else {
				boardList.setThumb("");
			}
			
			boardList.setName(getName(boardList.getId()));
			
			normal.add(boardList);
		}
		
		JSONObject json = new JSONObject();
		json.put("normal", normal);
		
		return json.toString();
	}
	
	// 게시글 전체
	public String selectMoimBoard(int moimcode, String url) {
		JSONObject tempFeel = new JSONObject(selectBoardFeel(moimcode, url));
		JSONObject tempNormal = new JSONObject(selectBoardNomal(moimcode, url));
		
		JSONArray feel = tempFeel.getJSONArray("feel");
		JSONArray normal = tempNormal.getJSONArray("normal");
		
		JSONObject json = new JSONObject();
		json.put("feel", feel);
		json.put("normal", normal);
		
		return json.toString();
	}
	
	
	// 특정 게시글 조회
	public String selectBoard(int listnum, String url) {
		String originurl = url + "/origin/";
		String thumurl = url + "/thum/";
		
		ArrayList<BoardList> items = new ArrayList<BoardList>();
		
		BoardList boardList = dao.selectBoard(listnum);
		
		if (boardList.getFilename() != null) {
			boardList.setFilename(originurl + boardList.getFilename());
		} else {
			boardList.setFilename("");
		}
		if (boardList.getThumb() != null) {
			boardList.setThumb(thumurl + boardList.getThumb());
		} else {
			boardList.setThumb("");
		}
		
		boardList.setName(getName(boardList.getId()));
		
		items.add(boardList);
		
		JSONObject json = new JSONObject();
		
		json.put("items", items);
		
		return json.toString();
	}
	
	// 덧 글 작성
	public String insertReDat(ReDat reDat) {
		String result;
		
		if (dao.insertReDat(reDat) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 덧 글 수정
	public String updateReDat(ReDat reDat) {
		String result;
		
		if (dao.updateReDat(reDat) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 덧 글 삭제
		// 특정 덧 글 삭제
	public String deleteRedat(int renum) {
		String result;
		if (dao.deleteRedat(renum) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 모임 덧 글 삭제
	public String deleteMoimRedat(int moimcode) {
		String result;
		if (dao.deleteMoimRedat(moimcode) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 사용자 덧 글 삭제
	public String deleteUserRedat(String id) {
		String result;
		if (dao.deleteUserRedat(id) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 원 글 에 종속된 덧 글 삭제
	public String deleteBoardReDat(int listnum) {
		String result;
		if (dao.deleteBoardReDat(listnum) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 모임내 사용자 덧 글 삭제
	public String deleteMoimUserRedat(MoimMem moimMem) {
		String result;
		if (dao.deleteMoimUserRedat(moimMem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 덧 글 조회
		// 원 글에 종속된 덧글 전체 조회
	public String selectReDatList(int listnum) {
		JSONObject json = new JSONObject();
		
		List<ReDat> list = dao.selectReDatList(listnum);
		
		JSONArray items = new JSONArray(list);
		
		json.put("items", items);
		
		return json.toString();
	}
	
		// 특정 덧 글 조회
	public String selectReDat(int renum) {
		JSONObject json = new JSONObject();
		
		ReDat reDat = dao.selectRedat(renum);
		
		JSONObject temp = new JSONObject();
		temp.put("renum", reDat.getRenum());
		temp.put("listnum", reDat.getListnum());
		temp.put("moimcode", reDat.getMoimcode());
		temp.put("id", reDat.getId());
		temp.put("redate", reDat.getRedate());
		temp.put("reple", reDat.getReple());
		
		json.put("item", temp);
		
		return json.toString();
	}
	
	
/* 일정 */	
	// 일정 등록
	public String insertSchedule(Schedule schedule) {
		String result;
		
		if (dao.insertSchedule(schedule) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 일정 수정
	public String updateSchedule(Schedule schedule) {
		String result;
		
		if (dao.updateSchedule(schedule) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 일정 삭제
		// 모임 일정 전체 삭제 : 모임 해체
	public String deleteMoimSchedule(int moimcode) {
		String result;
		
		List<Schedule> list = dao.selectMoimSchedule(moimcode);
		
		if (dao.deleteMoimSchedule(moimcode) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		for (Schedule schedule : list) {
			deleteSchememSche(schedule.getSch_schnum());
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 단일 일정 삭제
	public String deleteSchedule(int schnum) {
		String result;
		
		if (dao.deleteSchedule(schnum) > 0) {
			deleteSchememSche(schnum);
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 일정 조회
		// 일정 전체 조회
	public String selectMoimSchedule(int moimcode) {
		JSONObject json = new JSONObject();
		
		List<Schedule> list = dao.selectMoimSchedule(moimcode);
		
		JSONArray items = new JSONArray(list);
		
		json.put("items", items);
		
		return json.toString();
	}
	
		// 월간 일정 조회
	public String selectScheduleMonth(Map<String, Object> map) {
		JSONObject json = new JSONObject();
		
		List<Schedule> list = dao.selectScheduleMonth(map);
		
		JSONArray items = new JSONArray(list);
		
		json.put("items", items);
		
		return json.toString();
	}
	
		// 일정 상세 조회
	public String selectSchedule(int schnum) {
		JSONObject json = new JSONObject();
		
		Schedule schedule = dao.selectSchedule(schnum);
		
		JSONObject temp = new JSONObject();
		temp.put("schnum", schedule.getSch_schnum());
		temp.put("moimcode", schedule.getSch_moimcode());
		temp.put("year", schedule.getSch_year());
		temp.put("month", schedule.getSch_month());
		temp.put("day", schedule.getSch_day());
		temp.put("time", schedule.getSch_time());
		temp.put("title", schedule.getSch_title());
		temp.put("sub", schedule.getSch_sub());
		temp.put("amount", schedule.getSch_amount());
		temp.put("lat", schedule.getSch_lat());
		temp.put("lot", schedule.getSch_lot());
		
		json.put("item", temp);
		
		return json.toString();
	}
	
	
/* 일정 멤버 */
	// 일정 멤버 입력
	public String insertSchemem(Schemem schemem) {
		String result;
		
		if (dao.insertSchemem(schemem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 일정 멤버 수정
		// 일정 참가자 내용 수정
	public String updateSchemem(Schemem schemem) {
		String result;
		
		JSONObject old = new JSONObject(selectSchememUser(schemem));
		JSONObject item = old.getJSONObject("item");
		
		if (schemem.getAmount().equals("")) {
			schemem.setAmount(item.getString("amount"));
		}
		
		if (schemem.getIspay().equals("")) {
			schemem.setIspay(item.getString("ispay"));
		}

		if (schemem.getTodo().equals("")) {
			schemem.setTodo(item.getString("todo"));
		}
		
		if (schemem.getIsdo().equals("")) {
			schemem.setIsdo(item.getString("isdo"));
		}

		if (schemem.getEx().equals("")) {
			schemem.setEx(item.getString("ex"));
		}
		
		if (dao.updateSchemem(schemem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 회비 액수 변경
	public String updateAmount(Schemem schemem) {
		String result;
		
		if (dao.updateAmount(schemem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 할 일 변경
	public String updateToDo(Schemem schemem) {
		String result;
		
		if (dao.updateToDo(schemem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 사용자 회비 납부 여부 갱신
	public String updateIsPay(Schemem schemem) {
		String result;
		
		if (dao.updateIsPay(schemem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 사용자 수행여부 갱신
	public String updateIsDo(Schemem schemem) {
		String result;
		
		if (dao.updateIsDo(schemem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
	// 삭제
		// 일정 멤버 삭제(사용자 불참)
	public String deleteSchemem(Schemem schemem) {
		String result;
		if (dao.deleteSchemem(schemem) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
		
		// 스케줄 삭제
	public String deleteSchememSche(int schnum) {
		String result;
		if (dao.deleteSchememSche(schnum) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
	
		// 멤버 탈퇴에 따른 스케줄 참가 해제
	public String deleteUserSchemem(String id) {
		String result;
		if (dao.deleteUserSchemem(id) > 0) {
			result = "OK";
		} else {
			result = "FAIL";
		}
		
		JSONObject json = new JSONObject();
		json.put("result", result);
		
		return json.toString();
	}
		
	// 조회
		// 일정 내 전체 참가자 정보 및 부가 데이터
	public String selectSchemem(int schnum) {
		JSONObject json = new JSONObject();
		
		List<Schemem> list = dao.selectSchemem(schnum);
		
		JSONArray items = new JSONArray();
		
		for (Schemem schemem : list) {
			JSONObject temp = new JSONObject(selectSchememUser(schemem));
			JSONObject item = temp.getJSONObject("item");
			
			items.put(item);
		}
		
		json.put("items", items);
		
		return json.toString();
	}
		
		// 일정내 특정 참가자 정보 조회
	public String selectSchememUser(Schemem schemem) {
		JSONObject json = new JSONObject();
		
		JSONObject user = new JSONObject(selectUser(schemem.getId()));
		JSONObject prop = user.getJSONObject("properties");
		
		Schemem mem = dao.selectSchememUser(schemem);
		
		if (mem != null) {
			JSONObject temp = new JSONObject();
			temp.put("schenum", mem.getSchnum());
			temp.put("id", mem.getId());
			temp.put("amount", mem.getAmount());
			temp.put("ex", mem.getEx());
			temp.put("isdo", mem.getIsdo());
			temp.put("ispay", mem.getIspay());
			temp.put("todo", mem.getTodo());
			
			if (prop.has("update_prof")) {
				temp.put("prof", prop.get("update_prof"));
			} else if (prop.has("thumbnail_image")) {
				temp.put("prof", prop.getString("thumbnail_image"));
			} else {
				temp.put("prof", "");
			}
			
			json.put("item", temp);
		} else {
			json.put("result", "false");
		}
		
		return json.toString();
	}
}