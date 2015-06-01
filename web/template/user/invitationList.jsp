<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="ar.edu.ubp.das.i18n.text" />

<c:set var="form" value="${requestScope['form']}" />
<c:set var="invitations" value="${form['invitations']}" />

<c:choose>
    <c:when test="${empty invitations}">
        <div class="col-sm-6 col-md-4">
            <div class="alert alert-warning">No invitations</div>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach var="invitation" items="${invitations}">
            <div class="col-sm-6 col-md-4" ng-controller="InvitationController" ng-init="init(${invitation.getId()})">
                <div class="thumbnail">
                    <img src="http://www.gettyimages.co.uk/gi-resources/images/Homepage/Category-Creative/UK/UK_Creative_462809583.jpg" alt="...">
                    <div class="caption">
                        <h3>Invitation from {{ sender.login }} to room {{ room.name }}</h3>
                        <p>State: ${invitation.getState()}</p>
                        <p>
                            <c:choose>
                                <c:when test="${invitation.getState().equals('pending')}">
                                    <a class="btn btn-success" role="button" ng-click="acceptInvitation()">
                                        <fmt:message key="app.button.accept"/>
                                    </a>
                                    <a class="btn btn-danger" role="button" ng-click="rejectInvitation()">
                                        <fmt:message key="app.button.reject"/>
                                    </a>
                                </c:when>
                                <c:when test="${invitation.getState().equals('accepted')}">
                                    <a class="btn btn-success" role="button" ng-click="CommandManager.execute('startRoomSession',room)">
                                        <fmt:message key="app.button.enter"/>
                                    </a>
                                </c:when>
                                <c:when test="${invitation.getState().equals('rejected')}">
                                    <a class="btn btn-danger" role="button">
                                        <fmt:message key="app.button.delete"/>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>
            </div> 
        </c:forEach>
    </c:otherwise>
</c:choose>