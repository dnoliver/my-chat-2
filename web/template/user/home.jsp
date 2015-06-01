<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="ar.edu.ubp.das.i18n.text" />

<c:set var="form" value="${requestScope['form']}" />
<c:set var="profile" value="${form['profile']}" />

<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12 col-md-12">
            <div class="page-header">
                <h1>
                    <fmt:message key="user.home.title"/> 
                    <small>
                        <fmt:message key="user.home.subtitle"/>
                    </small>
                </h1>
            </div>
        </div>
    </div>
    
    <!-- Pills Menu -->
    <div class="row">
        <div class="col-xs-12 col-md-6">
            <ul class="nav nav-pills" role="tablist">
                <li role="presentation" class="active">
                    <a href="" data-target="#home" aria-controls="home" role="tab" data-toggle="pill">
                        <fmt:message key="user.home.tab.home.title"/>
                    </a>
                </li>
                <li role="presentation">
                    <a href="" data-target="#publicRooms" aria-controls="publicRooms" role="tab" data-toggle="pill" ng-click="loadPublicRooms()">
                        <fmt:message key="user.home.tab.rooms.title"/>
                    </a>
                </li>
                <li role="presentation">
                    <a href="" data-target="#privateRooms" aria-controls="privateRooms" role="tab" data-toggle="pill" ng-click="loadPrivateRooms()">
                        <fmt:message key="user.home.tab.chats.title"/>
                    </a>
                </li>
                <li role="presentation">
                    <a href="" data-target="#invitations" aria-controls="invitations" role="tab" data-toggle="pill" ng-click="loadInvitations()">
                        <fmt:message key="user.home.tab.invitations.title"/>
                    </a>
                </li>
                <li role="presentation">
                    <a href="" data-target="#search" aria-controls="search" role="tab" data-toggle="pill" ng-click="loadSearch()">
                        <fmt:message key="user.home.tab.search.title"/>
                    </a>
                </li>
            </ul>
        </div>
    </div>
    
    <br>
    
    <div class="row">
        <div class="col-xs-12 col-md-12">
            <div class="tab-content">
                
                <!-- Home Panel -->
                <div role="tabpanel" class="tab-pane active" id="home">
                    <div class="jumbotron">
                        <h1>
                            <fmt:message key="user.home.tab.home.welcome"/>
                        </h1>
                        <p>
                            <fmt:message key="user.home.tab.home.banner"/>
                        </p>
                        <form class="form-inline" ng-controller="CreateRoomController" ng-init="init(${profile.getId()})">
                            <div class="form-group">
                              <div class="input-group">
                                <div class="input-group-addon">@title</div>
                                <input type="text" class="form-control" ng-model="room.name">
                              </div>
                            </div>
                            <button type="submit" class="btn btn-primary" ng-click="createPrivateRoom()">Create</button>
                        </form>
                    </div>
                </div>
                
                <!-- Public Rooms Panel -->
                <div role="tabpanel" class="tab-pane" id="publicRooms">
                    <h3>
                        <fmt:message key="user.home.tab.rooms.title"/>
                    </h3>

                    <div class="row" id="publicRoomsList"></div>
                </div>
                
                <!-- Private Rooms Panel -->
                <div role="tabpanel" class="tab-pane" id="privateRooms">
                    <h3>
                        <fmt:message key="user.home.tab.chats.title"/>
                    </h3>
                    
                    <div class="row" id="privateRoomsList"></div>
                </div>
                
                <!-- Invitations Panel -->
                <div role="tabpanel" class="tab-pane" id="invitations">
                    <h3>
                        <fmt:message key="user.home.tab.invitations.title"/>
                    </h3>
                    
                    <div class="row" id="invitationsList"></div>
                </div>
                    
                <!-- Search Panel -->
                <div role="tabpanel" class="tab-pane" id="search" ng-controller="SearchController" ng-init="init(${profile.getId()})">
                    <h3>
                        <fmt:message key="user.home.tab.search.title"/>
                    </h3>
                    
                    <div class="row">
                        <div class="col-xs-12">
                            <form class="form-inline">
                                <div class="form-group">
                                    <input type="text" class="form-control" placeholder="Search Message Body" ng-model="search.body">
                                </div>
                            </form>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-xs-12">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>id</th>
                                        <th>room</th>
                                        <th>owner</th>
                                        <th>datetimeOfCreation</th>
                                        <th>body</th>
                                        <th>state</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="message in messages | filter:search:strict">
                                        <td>{{ message.id }}</td>
                                        <td>{{ message.room }}</td>
                                        <td>{{ message.owner }}</td>
                                        <td>{{ message.datetimeOfCreation }}</td>
                                        <td>{{ message.body }}</td>
                                        <td>{{ message.state }}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
