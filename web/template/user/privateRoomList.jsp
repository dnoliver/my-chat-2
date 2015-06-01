<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="ar.edu.ubp.das.i18n.text" />

<c:set var="form" value="${requestScope['form']}" />
<c:set var="privateRooms" value="${form['privateRooms']}" />

<c:choose>
    <c:when test="${empty privateRooms}">
        <div class="col-sm-6 col-md-4">
            <div class="alert alert-warning">No private rooms</div>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach var="room" items="${privateRooms}">
            <div class="col-sm-6 col-md-4" ng-controller="RoomItemController" ng-init="init(${room.getId()})">
                <div class="thumbnail">
                    <a href=""  ng-click="CommandManager.execute('startRoomSession',room)">
                        <img src="http://www.joomlaworks.net/images/demos/galleries/abstract/7.jpg" alt="...">
                        <div class="caption">
                            <h3>${room.getName()}</h3>
                        </div>
                    </a>
                </div>
            </div> 
        </c:forEach>
    </c:otherwise>
</c:choose>