<%@page import="java.io.File"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String originalname = "asdf.dddd";
	String filetype = originalname.substring(originalname.lastIndexOf('.'));
	String filename = originalname.substring(0, originalname.lastIndexOf('.'));
	
	SimpleDateFormat time = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
	
	String result = time.format(System.currentTimeMillis());
	
	String path = getServletContext().getRealPath("/storage");
	
	String temp = "http://" + request.getServerName() + ":" 
	+ request.getServerPort() +  request.getContextPath();
	
	String testt = request.getRealPath("/storage");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%= filetype %>
<br>
<%= filename %>
<br>
<%= path %>
<br>
<%= temp %>
</body>
</html>