<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="ar.edu.ubp.das.i18n.text" />

<!doctype html>
<html lang="en" ng-app="App">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    
    <title><fmt:message key="app.title"/></title>
    
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="css/main.css">
    
    <script src="thirdparty/angular.min.js"></script>
    <script src="thirdparty/angular-route.min.js"></script>
    <script src="thirdparty/angular-resource.min.js"></script>
    <script src="thirdparty/underscore.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.2.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    
    <script src="js/commons.js"></script>
    <script src="js/app.js"></script>
    <script src="js/rest.js"></script>
    <script src="js/session.js"></script>
    <script src="js/controllers.js"></script>
    <script src="js/command.js"></script>
</head>
<body ng-controller="MainController">
    <nav class="navbar navbar-default navbar-fixed-top navbar-inverse">
        <div class="container-fluid">
          <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#main-navbar">
                    <span class="sr-only">
                        <fmt:message key="app.navbar.toggleNavigation"/>
                    </span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="" ng-show="!isLoggedInRoom">
                    <fmt:message key="app.name"/>
                </a>
                <a class="navbar-brand" href="" ng-show="isLoggedInRoom">
                    {{ roomTitle }}
                </a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="main-navbar">
                <form class="navbar-form navbar-right" ng-show="isLoggedIn === false">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="<fmt:message key="login.placeholder.username"/>" ng-model="profile.login">
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" placeholder="<fmt:message key="login.placeholder.password"/>" ng-model="profile.password">
                    </div>
                    <button type="submit" class="btn btn-primary navbar-button" ng-click="CommandManager.execute('startSession',profile)">
                        <fmt:message key="login.button.login"/>
                    </button>
                </form>
                
                <p class="navbar-text" ng-show="isLoggedIn">{{ Session.profile.login }}</p>
                
                <button ng-show="isLoggedIn && !isLoggedInRoom" class="btn btn-primary navbar-btn" type="button" ng-click="CommandManager.execute('endSession')">
                    <fmt:message key="login.button.logout"/>
                </button>
                
                <button ng-show="isLoggedInRoom" type="button" class="btn btn-danger navbar-btn" ng-click="CommandManager.execute('endRoomSession')">
                    <fmt:message key="room.button.leave"/>
                </button>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>
    
    <div class="modal fade" id="sessionExpireModal" tabindex="-1" role="dialog" aria-labelledby="sessionExpireModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="sessionExpireModalLabel">
                         <fmt:message key="app.sessionExpired.title"/>
                    </h4>
                </div>
                <div class="modal-body">
                    <fmt:message key="app.sessionExpired.message"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal" ng-click="CommandManager.execute('endSession')">
                        <fmt:message key="app.button.ok"/>
                    </button>
                </div>
            </div>
        </div>
    </div>
                    
    <div class="modal fade" id="loadingModal" tabindex="-1" role="dialog" aria-labelledby="loadingModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="loadingModalLabel">
                        <fmt:message key="app.loading.title"/>
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="progress">
                        <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                          <span class="sr-only"><fmt:message key="app.loading.title"/></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div ng-view>
        <!-- View is loaded here -->
    </div>
</body>
</html>
