<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="ar.edu.ubp.das.i18n.text" />

<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12 col-md-12">
            <div class="page-header">
                <h1>
                    <fmt:message key="admin.home.title"/> 
                    <small><fmt:message key="admin.home.subtitle"/></small>
                </h1>
            </div>
        </div>
    </div>
    
    <!-- Pills Menu -->
    <div class="row">
        <div class="col-xs-12">
            <ul class="nav nav-pills" role="tablist">
                <li role="presentation" class="active">
                    <a href="" data-target="#profiles" aria-controls="profiles" role="tab" data-toggle="pill" ng-click="AdminSession.updateProfiles()">
                        <fmt:message key="admin.home.tab.profiles.title"/>
                    </a>
                </li>
                <li role="presentation">
                    <a href="" data-target="#rooms" aria-controls="rooms" role="tab" data-toggle="pill" ng-click="AdminSession.updateRooms()">
                        <fmt:message key="admin.home.tab.rooms.title"/>
                    </a>
                </li>
                <li role="presentation">
                    <a href="" data-target="#messages" aria-controls="messages" role="tab" data-toggle="pill" ng-click="AdminSession.updateMessages()">
                        <fmt:message key="admin.home.tab.messages.title"/>
                    </a>
                </li>
                <li role="presentation">
                    <a href="" data-target="#invitations" aria-controls="invitations" role="tab" data-toggle="pill" ng-click="AdminSession.updateInvitations()">
                        <fmt:message key="admin.home.tab.invitations.title"/>
                    </a>
                </li>
                <li role="presentation">
                    <a href="" data-target="#userslogins" aria-controls="profiles" role="tab" data-toggle="pill" ng-click="AdminSession.updateUsersLogins()">
                        <fmt:message key="admin.home.tab.usersLogins.title"/>
                    </a>
                </li>
                <li role="presentation">
                    <a href="" data-target="#usersaccess" aria-controls="profiles" role="tab" data-toggle="pill" ng-click="AdminSession.updateUsersAccess()">
                        <fmt:message key="admin.home.tab.usersAccess.title"/>
                    </a>
                </li>
                <li role="presentation">
                    <a href="" data-target="#roomsaccesspolicy" aria-controls="roomsaccesspolicy" role="tab" data-toggle="pill" ng-click="AdminSession.updateRoomsAccessPolicy()">
                        <fmt:message key="admin.home.tab.roomsAccessPolicy.title"/>
                    </a>
                </li>
            </ul>
        </div>
    </div>
    
    <br>
    
    <div class="row">
        <div class="col-xs-12 col-md-12">
            <div class="tab-content">
                
                <!-- profiles Panel -->
                <div role="tabpanel" class="tab-pane active" id="profiles">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>logins</th>
                                <th>password</th>
                                <th>type</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="p in AdminSession.profiles" ng-controller="ProfileController" ng-init="init(p.id)">
                                <td>{{ profile.id }}</td>
                                <td>{{ profile.login }}</td>
                                <td>{{ profile.password }}</td>
                                <td>
                                    <input type="text" ng-model="profile.type">
                                    <a href='' ng-click="update()">+</a>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                <!-- rooms  Panel -->
                <div role="tabpanel" class="tab-pane" id="rooms">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>name</th>
                                <th>type</th>
                                <th>owner</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="r in AdminSession.rooms">
                                <td>
                                    {{ r.id }}
                                    <a href='' 
                                       ng-show="r.type === 'public'"
                                       ng-click="CommandManager.execute('startRoomSession',r)">+</a>
                                </td>
                                <td>{{ r.name }}</td>
                                <td>{{ r.type }}</td>
                                <td>{{ r.owner }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                <!-- messages  Panel -->
                <div role="tabpanel" class="tab-pane" id="messages">
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
                            <tr ng-repeat="m in AdminSession.messages">
                                <td>{{ m.id }}</td>
                                <td>{{ m.room }}</td>
                                <td>{{ m.owner }}</td>
                                <td>{{ m.datetimeOfCreation }}</td>
                                <td>{{ m.body }}</td>
                                <td>{{ m.state }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                <!-- invitations Panel -->
                <div role="tabpanel" class="tab-pane" id="invitations">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>room</th>
                                <th>sender</th>
                                <th>receiver</th>
                                <th>state</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="i in AdminSession.invitations">
                                <td>{{ i.id }}</td>
                                <td>{{ i.room }}</td>
                                <td>{{ i.sender }}</td>
                                <td>{{ i.receiver }}</td>
                                <td>{{ i.state }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                <!-- users logins Panel -->
                <div role="tabpanel" class="tab-pane" id="userslogins">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>profile</th>
                                <th>datetimeOfAccessStart</th>
                                <th>datetimeOfAccessEnd</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="u in AdminSession.userslogins">
                                <td>{{ u.id }}</td>
                                <td>{{ u.profile }}</td>
                                <td>{{ u.datetimeOfAccessStart }}</td>
                                <td>{{ u.datetimeOfAccessEnd }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                <!-- users access Panel -->
                <div role="tabpanel" class="tab-pane" id="usersaccess">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>room</th>
                                <th>profile</th>
                                <th>datetimeOfAccessStart</th>
                                <th>datetimeOfAccessEnd</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="u in AdminSession.usersaccess">
                                <td>{{ u.id }}</td>
                                <td>{{ u.room }}</td>
                                <td>{{ u.profile }}</td>
                                <td>{{ u.datetimeOfAccessStart }}</td>
                                <td>{{ u.datetimeOfAccessEnd }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                <!-- rooms access policy Panel -->
                <div role="tabpanel" class="tab-pane" id="roomsaccesspolicy">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>room</th>
                                <th>profile</th>
                                <th>policy</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="r in AdminSession.roomsaccesspolicy">
                                <td>{{ r.id }}</td>
                                <td>{{ r.room }}</td>
                                <td>{{ r.profile }}</td>
                                <td>{{ r.policy }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

