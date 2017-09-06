package com.wolfKill.servers;

import java.io.IOException;
import java.util.*;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.*;
import com.wolfKill.gameThread.GameThread;
import com.wolfKill.rooms.GameRoom;
import com.wolfKill.utils.AroundInfo;
import com.wolfKill.utils.JsonDataOperation;

@ServerEndpoint(value="/websocket")
public class GameServer {
	//游戏房间对象
	private static GameRoom room = new GameRoom("测试房2","测试新房间");
	
	@OnOpen
	public void open(Session session){
		System.out.println(session.getId()+ "已连接websocket");
	}

	@OnClose
	public void close(Session session){
		room.leaveRoom(session.getId());
	}
	
	@OnMessage
	public void OnMessage(String message, Session session){
		String type = JsonDataOperation.handleJSONData(message, "type");
		//判断信息是否为连接信息
		if(type.equals("video-offer") || type.equals("video-answer") || type.equals("icecandidate")){
			room.handleConnectedInfo(message,type);
		}
		//判断信息是否为初始化信息
		else if(type.equals("initInfo")){
			room.joinRoom(message, session);
		}
	}

}
