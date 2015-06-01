<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="ar.edu.ubp.das.i18n.text" />

<c:set var="form" value="${requestScope['form']}" />
<c:set var="participants" value="${form['participants']}" />
<c:set var="user" value="${form['user']}" />
<c:set var="room" value="${form['room']}" />

<c:choose>
    <c:when test="${empty participants}">
        <div class="alert alert-warning">
            <strong>No participants in this room</strong>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach var="profile" items="${participants}">
            <c:if test="${profile.getType().equals('user')}">
                <div class="list-group-item" ng-controller="ParticipantController" ng-init="init(${profile.getId()})">
                    <c:if test="${ user.getType().equals('admin') && room.getType().equals('public') }">
                        <div class="dropdown pull-right">
                            <span id="participantMenu" class="caret" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></span>
                            <ul class="dropdown-menu" role="menu" aria-labelledby="participantMenu">
                                <li role="presentation">
                                    <a role="menuitem" tabindex="-1" href="" ng-click="CommandManager.execute('addUserToBlackList',participant)">
                                        <fmt:message key="room.tab.participants.addToBlackList"/>
                                    </a>
                                </li>
                             </ul>
                        </div>
                    </c:if>
                    <h4 class="list-group-item-heading">
                        ${profile.getLogin()}
                    </h4>
                </div>
            </c:if>
        </c:forEach>
    </c:otherwise>
</c:choose>
