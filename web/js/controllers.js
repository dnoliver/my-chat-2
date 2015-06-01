/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var Controllers = angular.module('Controllers',[]);

Controllers.constant('max_idle_time',60 * 5);

Controllers.controller('MainController', ['$scope','$window','max_idle_time','CommandManager','Session','AdminSession','RoomSession', function($scope,$window,max_idle_time,CommandManager,Session,AdminSession,RoomSession){
    app = {
        CommandManager: CommandManager,
        Session: Session,
        AdminSession: AdminSession,
        RoomSession: RoomSession,
        scope: $scope
    };
    
    $scope.CommandManager = CommandManager;
    $scope.AdminSession = AdminSession;
    $scope.Session = Session;
    $scope.RoomSession = RoomSession;
    
    $scope.$watch(function(){
        return Session.profile;
    },function(){
        $scope.isVisitant = Session.profile === null;
        $scope.isUser = !$scope.isVisitant && Session.profile.type === 'user';
        $scope.isAdmin = !$scope.isVisitant && Session.profile.type === 'admin';
    });
    
    $scope.$watch(function(){
        return Session.userlogin;
    },function(){
        $scope.isLoggedIn = Session.userlogin !== null && !Session.userlogin.datetimeOfAccessEnd;
    });
    
    $scope.$watch(function(){
        return RoomSession.useraccess;
    },function(){
        $scope.isLoggedInRoom = RoomSession.useraccess !== null && !RoomSession.useraccess.datetimeOfAccessEnd;
    });
    
    $scope.$watch(function(){
        return RoomSession.room;
    },function(){
        $scope.isPublicRoom = RoomSession.room !== null && RoomSession.room.type === 'public';
        $scope.isPrivateRoom = RoomSession.room !== null && RoomSession.room.type === 'private';
        
        $scope.isLoadingRoom = RoomSession.room === null;
        
        if($scope.isLoadingRoom){
            $scope.roomTitle = '';
        }
        else {
            $scope.roomTitle = $scope.RoomSession.room.name;
        }
    });
    
    var _idleSecondsCounter = 0;
    
    $window.document.onclick = function() {
        _idleSecondsCounter = 0;
    };
    $window.document.onmousemove = function() {
        _idleSecondsCounter = 0;
    };
    $window.document.onkeypress = function() {
        _idleSecondsCounter = 0;
    };
    $window.window.setInterval(function(){
        _idleSecondsCounter++;
        
        if($scope.isLoggedIn && _idleSecondsCounter >= max_idle_time){
            localStorage.removeItem('mychat_session');
            localStorage.removeItem('mychat_room_session');
            
            $('#sessionExpireModal').modal({
                backdrop: 'static',
                keyboard: 'false'
            });
        }
    }, 1000); 
    
    $scope.$on('showLoading', function(){
        $('#loadingModal').modal({
            backdrop: 'static',
            keyboard: 'false'
        }); 
    });
    
    $scope.$on('hideLoading', function(){
        $('#loadingModal').modal('hide'); 
    });
    
    CommandManager.changeImplementation('Visitant');
    CommandManager.execute('goToHome');
    CommandManager.execute('startSession',{userlogin:Session.getFromLocalStorage()});
}]);

Controllers.controller('HomeController', ['$scope','$compile', function($scope,$compile){
    console.log('HomeController','init');
    var ViewContent = $("<div></div>");
    var ViewContainer = $("#HomeView");
    
    ViewContent.load("index.jsp?action=GetHomePage",function(){
        console.log('HomeController','load');
        ViewContainer.append($compile(ViewContent.html())($scope));
    });
}]);

Controllers.controller('ProfileHomeController', ['$scope','$compile','$routeParams','$location', function($scope,$compile,$routeParams,$location){
    console.log('ProfileHomeController','init');
    
    var ViewContent = $("<div></div>");
    var ViewContainer = $("#ProfileHomeView");
    var ProfileId = parseInt($routeParams.profileId);

    $scope.$emit('showLoading');
        
    ViewContent.load("index.jsp?action=GetUserHomePage&profile=" + ProfileId,function(){
        console.log('ProfileHomeController','load');
        ViewContainer.append($compile(ViewContent.html())($scope));
        $scope.loadPublicRooms();
        $scope.loadPrivateRooms();
        $scope.loadInvitations(); 
        $scope.$emit('hideLoading');
    });
    
    $scope.loadPublicRooms = function(){
        var PublicRoomsViewContent = $("<div></div>");
        var PublicRoomsViewContainer = $("#publicRoomsList");

        PublicRoomsViewContent.load("index.jsp?action=GetPublicRoomList",function(){
            PublicRoomsViewContainer.html($compile(PublicRoomsViewContent.html())($scope));
        });
    };
    
    $scope.loadPrivateRooms = function(){
        var PrivateRoomsViewContent = $("<div></div>");
        var PrivateRoomsViewContainer = $("#privateRoomsList");

        PrivateRoomsViewContent.load("index.jsp?action=GetPrivateRoomList&owner=" + ProfileId,function(){
            PrivateRoomsViewContainer.html($compile(PrivateRoomsViewContent.html())($scope));
        });
    };
    
    $scope.loadInvitations = function(){
        var InvitationsViewContent = $("<div></div>");
        var InvitationsViewContainer = $("#invitationsList");

        InvitationsViewContent.load("index.jsp?action=GetInvitationList&receiver=" + ProfileId,function(){
            InvitationsViewContainer.html($compile(InvitationsViewContent.html())($scope));
        });
    };
    
    $scope.loadSearch = function(){
      $scope.$broadcast('loadSearch');
    };
    
    $scope.$on('loadInvitations',function(){
        $scope.loadInvitations();
    });
}]);

Controllers.controller('AdminHomeController', ['$scope','$compile', function($scope,$compile){
    console.log('AdminHomeController','init');
    
    var ViewContent = $("<div></div>");
    var ViewContainer = $("#AdminHomeView");
    
    ViewContent.load("index.jsp?action=GetAdminHomePage",function(){
        console.log('AdminHomeController','load');
        ViewContainer.append($compile(ViewContent.html())($scope));
    });
}]);

Controllers.controller('RoomHomeController',['$scope','$compile','$rest','$routeParams','$location', function($scope,$compile,$rest,$routeParams,$location){
    console.log('RoomHomeController','init');
    
    var ViewContent = $("<div></div>");
    var ViewContainer = $("#RoomHomeView");
    var RoomId = parseInt($routeParams.roomId);
    var ProfileId = parseInt($routeParams.profileId);
    var reloadInterval = null;
    
    $scope.$emit('showLoading');
    
    ViewContent.load("index.jsp?action=GetRoomPage&room=" + RoomId,function(){
        console.log('RoomHomeController','load');
        ViewContainer.append($compile(ViewContent.html())($scope));
        $scope.loadMessages();
        $scope.loadParticipants();
        $scope.checkAccessPolicy();
        $scope.$emit('hideLoading');
        
        reloadInterval = setInterval(function(){
            $scope.loadMessages();
            $scope.checkAccessPolicy();
        },10000);
        
        $scope.$watch(function(){ return $location.path(); }, function(newVal,oldVal){
            if(newVal !== oldVal){
                clearInterval(reloadInterval);
            } 
        });
    });
    
    $scope.loadMessages = function(){
        var MessagesViewContent = $("<div></div>");
        var MessagesViewContainer = $("#roomMessagesList");
    
        MessagesViewContent.load("index.jsp?action=GetMessageList&room=" + RoomId + "&profile=" + ProfileId,function(){
            MessagesViewContainer.html($compile(MessagesViewContent.html())($scope));
        });
    };
    
    $scope.loadParticipants = function(){
        var ParticipantsViewContent = $("<div></div>");
        var ParticipantsViewContainer = $("#roomParticipantsList");
    
        ParticipantsViewContent.load("index.jsp?action=GetParticipantsList&room=" + RoomId + "&profile=" + ProfileId,function(){
            ParticipantsViewContainer.html($compile(ParticipantsViewContent.html())($scope));
        });
    };
    
    $scope.checkAccessPolicy = function(){
        $rest.RoomAccessPolicy.findByRoom({room:RoomId},function(policies){
            for(var i = 0; i < policies.length; i++){
                if(policies[i].profile === ProfileId && policies[i].policy === 'reject'){
                    $('#roomModal').modal({
                        backdrop: 'static',
                        keyboard: 'false'
                    });
                    return;
                }
            }
        });
    };
}]);

Controllers.controller('RoomItemController', ['$scope','$rest', function($scope,$rest){  
    $scope.init = function(id){
        $scope.id = id;
        $rest.Room.get({id:id}, function(room){
            $scope.room = room;
        });
    };
}]);

Controllers.controller('CreateRoomController', ['$scope','$rest', function($scope,$rest){  
    $scope.init = function(id){
        $scope.profile = $rest.Profile.get({id:id}, function(){
            $scope.room = {
                owner: id,
                type: 'private'
            };
        });
    };
    
    $scope.createPrivateRoom = function(){
        $rest.Room.save($scope.room, function(){
           $scope.room.name = ''; 
        });
    };
}]);

Controllers.controller('SendInvitationController', ['$scope','$rest', function($scope,$rest){  
    $scope.init = function(id){
        $scope.room = $rest.Room.get({id:id}, function(room){
            $scope.invitation = {
                room: room.id,
                sender: room.owner,
                state: 'pending'
            };
            
            $scope.profile = {};
        });
    };
    
    $scope.sendInvitation = function(){
        $rest.Profile.findByLogin($scope.profile,function(profile){
            $scope.invitation.receiver = profile.id;
            $rest.Invitation.save($scope.invitation, function(){
                $scope.profile.login = '';
            }, function(){
            });
        }, function(){
            
        });
    };
}]);

Controllers.controller('InvitationController', ['$scope','$rest', function($scope,$rest){
    $scope.init = function(id){
        $scope.id = id;
        $scope.invitation = $rest.Invitation.get({id:id}, function(){
            $scope.sender = $rest.Profile.get({id:$scope.invitation.sender},function(){});
            $scope.receiver = $rest.Profile.get({id:$scope.invitation.receiver},function(){});
            $scope.room = $rest.Room.get({id:$scope.invitation.room},function(){});
        });
    };
    
    $scope.acceptInvitation = function(){
        $scope.invitation.state = 'accepted';
        $scope.invitation.$update(function(){ $scope.$emit('loadInvitations'); });
    };
    
    $scope.rejectInvitation = function(){
        $scope.invitation.state = 'rejected';
        $scope.invitation.$update(function(){ $scope.$emit('loadInvitations'); });
    };
}]);

Controllers.controller('MessageController', ['$scope','$rest', function($scope,$rest){
    $scope.init = function(id){
        $scope.id = id;
        $scope.message = $rest.Message.get({id:id});
    };
}]);

Controllers.controller('ParticipantController', ['$scope','$rest', function($scope,$rest){
    $scope.init = function(id){
        $scope.id = id;
        $scope.participant = $rest.Profile.get({id:id});
    };
}]);

Controllers.controller('ProfileController', ['$scope','$rest', function($scope,$rest){
    $scope.init = function(id){
        $scope.id = id;
        $scope.profile = $rest.Profile.get({id:id});
    };
    
    $scope.update = function(){
        $scope.profile.$update();
    };
}]);

Controllers.controller('SearchController', ['$scope','$rest', function($scope,$rest){
    $scope.init = function(id){
        $scope.id = id;
        $scope.messages = $rest.Message.findByOwner({owner:id});
        
        $scope.$on('loadSearch', function(){
            $scope.messages = $rest.Message.findByOwner({owner:$scope.id});
        });
    };
}]);