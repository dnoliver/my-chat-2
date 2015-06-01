<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="ar.edu.ubp.das.i18n.text" />

<c:set var="form" value="${requestScope['form']}" />
<c:set var="messages" value="${form['messages']}" />
<c:set var="user" value="${form['user']}" />

<c:choose>
    <c:when test="${empty messages}">
        <div class="alert alert-warning">
            <strong>
                <fmt:message key="room.tab.messages.noMessages"/>
            </strong>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach var="message" items="${messages}">
            <c:choose>
                <c:when test="${message.getState().equals('active')}">
                    <div ng-controller="MessageController" ng-init="init(${message.getId()})">
                        <div class="alert alert-info">
                            <c:if test="${user.getType().equals('admin')}">
                                <div class="dropdown pull-right">
                                    <span id="messageMenu" class="caret" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></span>
                                    <ul class="dropdown-menu" role="menu" aria-labelledby="messageMenu">
                                        <li role="presentation">
                                            <a role="menuitem" tabindex="-1" href="" ng-click="CommandManager.execute('removeMessage',message)">
                                                <fmt:message key="app.button.disable"/>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </c:if>
                            <span>
                                <strong>${message.getBody()}</strong>
                            </span>
                        </div>
                    </div>
                </c:when>
                <c:when test="${message.getState().equals('deleted')}">
                    <div ng-controller="MessageController" ng-init="init(${message.getId()})">
                        <div class="alert alert-danger">
                            <c:if test="${user.getType().equals('admin')}">
                                <div class="dropdown pull-right">
                                    <span id="messageMenu" class="caret" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></span>
                                    <ul class="dropdown-menu" role="menu" aria-labelledby="messageMenu">
                                        <li role="presentation">
                                            <a role="menuitem" tabindex="-1" href="" ng-click="CommandManager.execute('enableMessage',message)">
                                                <fmt:message key="app.button.enable"/>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </c:if>
                            <span>
                                <strong><fmt:message key="message.state.delete.text"/></strong>
                            </span>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:otherwise>
</c:choose>
