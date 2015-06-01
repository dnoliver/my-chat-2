<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="ar.edu.ubp.das.i18n.text" />

<c:set var="form" value="${requestScope['form']}" />
<c:set var="room" value="${form['room']}" />

<div class="modal fade" id="roomModal" tabindex="-1" role="dialog" aria-labelledby="roomModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="roomModalLabel">
                    <fmt:message key="app.accessDenied.title"/>
                </h4>
            </div>
                <div class="modal-body">
                    <fmt:message key="app.accessDenied.message"/>
                </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal" ng-click="CommandManager.execute('endRoomSession')">
                    <fmt:message key="app.button.ok"/>
                </button>
            </div>
        </div>
    </div>
</div>

<div class="container-fluid">

    <div class="row">
        <div class="col-xs-12 col-md-6">
            <ul class="nav nav-pills" role="tablist">
                <li role="presentation" class="active">
                    <a href="" data-target="#roomMessages" aria-controls="home" role="tab" data-toggle="pill" ng-click="loadMessages()">
                        <fmt:message key="room.tab.messages.title"/>
                    </a>
                </li>
                <li role="presentation">
                    <a href="" data-target="#roomParticipants" aria-controls="publicRooms" role="tab" data-toggle="pill" ng-click="loadParticipants()">
                        <fmt:message key="room.tab.participants.title"/>
                    </a>
                </li>
            </ul>
        </div>
    </div>
    
    <div class="row">
        <div class="col-xs-12 col-md-12">
            <div class="tab-content">
                
                <!-- Messages Panel -->
                <div role="tabpanel" class="tab-pane active" id="roomMessages">
                    <h3>
                        <fmt:message key="room.tab.messages.title"/>
                    </h3>
                    <div id="roomMessagesList"></div>
                </div>
                
                <!-- Participants Panel -->
                <div role="tabpanel" class="tab-pane" id="roomParticipants">
                    <h3>
                        <fmt:message key="room.tab.participants.title"/>
                    </h3>
                        <c:if test="${room.getType().equals('private')}">
                            <form class="form-inline" ng-controller="SendInvitationController" ng-init="init(${room.getId()})">
                                <div class="form-group">
                                  <div class="input-group">
                                    <div class="input-group-addon">@user</div>
                                    <input type="text" class="form-control" ng-model="profile.login">
                                    <span class="input-group-btn">
                                        <button class="btn btn-primary" type="button" ng-click="sendInvitation()">Send Invitation</button>
                                    </span>
                                  </div>
                                </div>
                            </form>
                            <br>
                        </c:if>
                    <div class="list-group" id="roomParticipantsList"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<nav class="navbar navbar-default navbar-fixed-bottom">
    <div class="container-fluid">
        <form class="navbar-form navbar-left">
            <div class="form-group">
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="message" ng-model="message.body">
                    <span class="input-group-btn">
                        <button type="submit" class="btn btn-success" ng-click="CommandManager.execute('postMessage',message)">
                            <fmt:message key="app.button.post"/>
                        </button>
                    </span>
                </div>
            </div>
        </form>
    </div>
</nav>
