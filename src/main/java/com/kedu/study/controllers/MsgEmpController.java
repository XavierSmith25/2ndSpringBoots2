package com.kedu.study.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kedu.study.dto.ChattingRoomDTO;
import com.kedu.study.dto.MessageDTO;
import com.kedu.study.dto.MsgEmpDTO;
import com.kedu.study.dto.MsgEmpMineDTO;
import com.kedu.study.service.MsgEmpService;

@Controller
@RequestMapping("/Employee")
public class MsgEmpController {



	@Autowired
	private MsgEmpService eServ;


	@GetMapping("/SelectEmp")
	public ResponseEntity<List<MsgEmpDTO>> select(){
		List<MsgEmpDTO> list = eServ.select();

		return ResponseEntity.ok(list);
	}

	@GetMapping("/SelectMine")
	public ResponseEntity<MsgEmpMineDTO> selectMine(@RequestParam String userId) {
		System.out.println("응답");
		MsgEmpMineDTO result = eServ.selectMine(userId);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/madeChatRoom")
	public ResponseEntity<Map<String,Object>> madeChatRoom(@RequestBody Map<String,Object> names){
		eServ.madeChatRoom(names);
		Integer seq = (Integer) names.get("seq");
		Map<String,Object> response = new HashMap<>();
		response.put("seq", seq);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/checkRoomExist")
	public ResponseEntity<Boolean> checkRoomExist(
			@RequestParam String targetname,
			@RequestParam String myname) {

		boolean exists = eServ.checkRoomExist(targetname, myname);
		return ResponseEntity.ok(exists);  // true 또는 false 반환
	}

	@GetMapping("/checkRoomSeqExist")
	public ResponseEntity<Map<String,Object>> checkRoomSeqExist(@RequestParam int targetId,
			@RequestParam int myId){
		Map<String,Object> seq = eServ.checkRoomSeqExist(targetId,myId);
		return ResponseEntity.ok(seq);
	}

	@GetMapping("/showMessages")
	public ResponseEntity<List<MessageDTO>> showMessages(@RequestParam int seq){
		List<MessageDTO> list = eServ.showMessages(seq);
		return ResponseEntity.ok(list);
	}

	@GetMapping("/selectMyId")
	public ResponseEntity<Integer> selectMyId(@RequestParam String userId){
		int id = eServ.selectMyId(userId);
		return ResponseEntity.ok(id);
	}

	@GetMapping("/selectRoom")
	public ResponseEntity<List<Map<String,Object>>> selectRoom(@RequestParam int myId){

		List<Map<String,Object>> list = eServ.selectRoom(myId);
		return ResponseEntity.ok(list);
	}

	@PostMapping("/madeGroupChat")
	public ResponseEntity<?> madeGroupChat(@RequestBody Map<String,Object> map){
		Integer myId =(Integer) map.get("myId");
		List<Integer> selected = (List<Integer>)map.get("selected");
		String selectedStr = selected.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
		List<Integer> allMembers = new ArrayList<>();
		allMembers.addAll(selected);
		allMembers.add(myId);
		Integer numMembers = allMembers.size();
		int seq = eServ.madeGroupChat(selectedStr,myId,numMembers);
		Map<String,Object> response = new HashMap<>();
		response.put("seq", seq);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/selectGroupChat")
	public ResponseEntity<List<ChattingRoomDTO>> selectGroupChat(@RequestParam int myId){
		List<ChattingRoomDTO> room = eServ.selectGroupChat(myId);
		
		return ResponseEntity.ok(room);
	}
	
	@PostMapping("/getNamesIds")
	public ResponseEntity<?> getNamesIds(@RequestBody Map<String,List<String>> map){
		List<String> ids = map.get("ids");
		List<String> list = eServ.getNamesIds(ids);
		
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/getGroupInfo")
	public ResponseEntity<List<Map<String,Object>>> getGroupInfo(@RequestParam List<Integer> groupId){
			List<Map<String,Object>> list = eServ.getGroupInfo(groupId);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/inviteToChat")
	public ResponseEntity<?> inviteToChat(@RequestBody Map<String,Object> data){
		Integer myId = (Integer)data.get("myId");
		List<Integer> selected = (List<Integer>)data.get("selected");
		List<Integer> empId = (List<Integer>)data.get("empId");
		
		List<Integer> mergedList = new ArrayList<>();
		mergedList.add(myId);
		mergedList.addAll(selected);
		mergedList.addAll(empId);
		
		int seq = eServ.inviteToChat(data,mergedList);
		
		return ResponseEntity.ok(seq);
	}
	
	@PutMapping("/quitRoom")
	public ResponseEntity<?> quitRoom(@RequestBody Map<String,Object> data){
		Integer myId = (Integer)data.get("myId");
	    Integer msgGroupId = (Integer)data.get("msgGroupId"); 
		eServ.quitRoom(myId,msgGroupId);
		
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/ProfileImg")
	public ResponseEntity<String> ProfileImg(@RequestParam("empId")int empId ){
			String profilePath = eServ.ProfileImg(empId);
			
			if(profilePath != null) {
				return ResponseEntity.ok(profilePath);
			} else {
				return ResponseEntity.ok("/files/upload/profile/Default2.png");
			}
			
	}
	
	@GetMapping("/AllProfileImg")
	public ResponseEntity<List<Map<String,Object>>> AllProfileImg(){
			List<Map<String,Object>> list = eServ.AllProfileImg();
		return ResponseEntity.ok(list);
	}
	

}
