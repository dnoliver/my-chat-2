/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var Session = angular.module('Session', ['Rest']);

Session.constant('fetch_interval', 20000);

Session.factory('AdminSession', ['fetch_interval', '$rest', function(fetch_interval,$rest){
    var AdminSession = {};
    
    AdminSession.profiles = null;
    AdminSession.rooms = null;
    AdminSession.messages = null;
    AdminSession.invitations = null;
    AdminSession.userslogins = null;
    AdminSession.usersaccess = null;
    AdminSession.roomsaccesspolicy = null;
    
    AdminSession.fetchInterval = null;
    
    AdminSession.startSession = function(profile,callback,error){
        AdminSession.updateSession(function(){
            //AdminSession.startFetching();
            callback && callback();
        },error);
    };
    
    AdminSession.endSession = function(callback,error){
        AdminSession.stopFetching();
        callback && callback();
    };
    
    AdminSession.startFetching = function(){
        if(AdminSession.fetchInterval !== null){
            return;
        }
        
        AdminSession.fetchInterval = setInterval(function(){
            AdminSession.updateSession(function(){
                console.log('AdminSession','updateSession');
            },function(){
                console.error('AdminSession','updateSession');
                AdminSession.stopFetching();
            });
        },fetch_interval);
    };
    
    AdminSession.stopFetching = function(){
        console.log('AdminSession','stopFetching');
        if(AdminSession.fetchInterval === null){
            return;
        }
        
        clearInterval(AdminSession.fetchInterval);
        AdminSession.fetchInterval = null;
    };
    
    AdminSession.updateProfiles = function(callback,error){
        console.log('AdminSession','updateProfiles');
        AdminSession.profiles = $rest.Profile.query(callback,error);
    };
    
    AdminSession.updateRooms = function(callback,error){
        console.log('AdminSession','updateRooms');
        AdminSession.rooms = $rest.Room.query(callback,error);
    };
    
    AdminSession.updateInvitations = function(callback,error){
        console.log('AdminSession','updateInvitations');
        AdminSession.invitations = $rest.Invitation.query(callback,error);
    };
    
    AdminSession.updateUsersLogins = function(callback,error){
        console.log('AdminSession','updateUsersLogins');
        AdminSession.userslogins = $rest.UserLogin.query(callback,error);
    };
    
    AdminSession.updateUsersAccess = function(callback,error){
        console.log('AdminSession','updateUsersAccess');
        AdminSession.usersaccess = $rest.UserAccess.query(callback,error);
    };
    
    AdminSession.updateRoomsAccessPolicy = function(callback,error){
        console.log('AdminSession','updateRoomsAccessPolicy');
        AdminSession.roomsaccesspolicy = $rest.RoomAccessPolicy.query(callback,error);
    };
    
    AdminSession.updateMessages = function(callback,error){
        console.log('AdminSession','updateMessages');
        AdminSession.messages = $rest.Message.query(callback,error);
    };
    
    AdminSession.updateSession = function(callback,error){
        console.log('AdminSession','updateSession');
        AdminSession.updateProfiles(function(){
            AdminSession.updateRooms(function(){
                AdminSession.updateInvitations(function(){
                    AdminSession.updateMessages(function(){
                        AdminSession.updateUsersLogins(function(){
                            AdminSession.updateUsersAccess(function(){
                                AdminSession.updateRoomsAccessPolicy(callback,error);
                            },error);
                        },error);
                    },error);
                },error);
            },error);
        },error);
    };
    
    return AdminSession;
}]);

Session.factory('Session', ['$rest','fetch_interval', function($rest,fetch_interval){
    var Session = {};
    
    Session.profile = null;
    Session.userlogin = null;
    Session.rooms = [];
    Session.chats = [];
    Session.invitations = [];
    Session.messages = [];
    
    Session.fetchInterval = null;
    
    Session.createProfile = function(person,callback,error){
        console.log("Session","createProfile");
        
        var profile = new $rest.Profile(person);
        profile.type = 'user';
        profile.$save(callback,error);
    };
    
    Session.createUserLogin = function(user,callback,error){
        console.log("Session","createUserLogin");
        $rest.Profile.findByLogin(user,function(profile){
            if(!profile.id ) {
                error('invalid profile');
                return;
            }
            
            if(user.password !== profile.password){
                error('invalid password');
                return;
            }
            
            $rest.UserLogin.save({
                profile: profile.id
            },callback,error);
        },error);
    };
    
    Session.createUserAccess = function(room,callback,error){
        console.log("Session","createUserAccess");
        $rest.UserAccess.save({
            room: room.id,
            profile: Session.profile.id
        }, callback,error);
    };
    
    Session.createChat = function(options,callback,error){
        console.log("Session","createChat");
        $rest.Room.save({
            name: (new Date()).getTime().toString(),
            owner: Session.profile.id,
            type: 'private'
        },callback,error);
    };
    
    Session.createInvitation = function(invitation,callback,error){
        console.log("Session","createInvitation");
        $rest.Invitation.save({
            sender: Session.profile.id,
            receiver: invitation.receiver,
            room: invitation.room,
            state: 'pending'
        },callback,error);
    };
    
    Session.createRoomAccessPolicy = function(accesspolicy,callback,error){
        console.log("Session","createRoomAccessPolicy");
        $rest.RoomAccessPolicy.save({
            room: accesspolicy.room,
            profile: accesspolicy.profile,
            policy: 'reject'
        },callback,error);
    };
    
    Session.acceptInvitation = function(invitation,callback,error){
        console.log("Session","acceptInvitation");
        invitation.state = 'accepted';
        invitation.$update(callback,error);
    };
    
    Session.rejectInvitation = function(invitation,callback,error){
        console.log("Session","rejectInvitation");
        invitation.state = 'rejected';
        invitation.$update(callback,error);
    };
    
    Session.getFromLocalStorage = function(){
        console.log("Session","getFromLocalStorage");
        var item = localStorage['mychat_session'];
        
        if(item){
            var userlogin = JSON.parse(item); 
            
            if(userlogin.datetimeOfAccessEnd){
                localStorage.removeItem('mychat_session');
                return null;
            }
            
            return userlogin;
        }
        else {
            null;
        }
    };
    
    Session.startSession = function(userlogin,callback,error){
        console.log("Session","startSession");
        Session.userlogin = $rest.UserLogin.get(userlogin,function(userlogin){
            Session.profile = $rest.Profile.get({id:userlogin.profile},function(profile){
                Session.updateSession(function(){
                    Session.startFetching();
                    callback && callback();
                },error);
            });
        },error);
    };
    
    Session.saveSession = function(callback,error){
        console.log("Session","saveSession");
        if(Session.userlogin){
            localStorage['mychat_session'] = JSON.stringify(Session.userlogin.toJSON());
        }
        callback();
    };
    
    Session.endSession = function(callback,error){
        console.log("Session","endSession");
        Session.stopFetching();
        Session.userlogin.$terminate(function(){
            localStorage.removeItem('mychat_session');
            Session.profile = null;
            Session.userlogin = null;
            Session.rooms = [];
            Session.chats = [];
            Session.invitations = [];
            Session.messages = [];
            callback && callback();
        },error);
    };
    
    Session.startFetching = function(){
        console.log("Session","startFetching");
        if(Session.fetchInterval !== null){
            return;
        }
        
        Session.fetchInterval = setInterval(function(){
            Session.updateSession(function(){
                console.log('Session','updated');
            },function(){
                console.error('Session','error in update');
                Session.stopFetching();
            });
        },fetch_interval);
    };
    
    Session.stopFetching = function(){
        console.log("Session","stopFetching");
        if(Session.fetchInterval === null){
            return;
        }
        
        clearInterval(Session.fetchInterval);
        Session.fetchInterval = null;
    };
    
    Session.updateProfile = function(callback,error){
       console.log("Session","updateProfile");
       Session.profile.$get(callback,error);
    };
    
    Session.updateUserLogin = function(callback,error){
       console.log("Session","updateUserLogin");
       Session.userlogin.$get(callback,error); 
    };
    
    Session.updateMessages = function(callback,error){
        console.log("Session","updateMessages");
        $rest.Message.findByOwner({owner:Session.profile.id},function(messages){
            Session.messages = messages;
            callback && callback();
        },error);
    };
    
    Session.updateRooms = function(callback,error){
        console.log("Session","updateRooms");
        $rest.Room.findPublics({type:'public'},function(rooms){
            Session.rooms = rooms;
            callback && callback();
        },error);
    };
    
    Session.updateChats = function(callback,error){
        console.log("Session","updateChats");
        $rest.Room.findByOwner({owner:Session.profile.id},function(chats){
            Session.chats = chats;
            callback && callback();
        },error);
    };
    
    Session.updateInvitations = function(callback,error){
        console.log("Session","updateInvitations");
        $rest.Invitation.findByReceiver({receiver:Session.profile.id},function(i){
            Session.invitations = i;
            callback && callback();
        },error);
    };
    
    Session.updateCollections = function(callback,error){
        console.log("Session","updateCollections");
        Session.updateRooms(function(){
            Session.updateChats(function(){
                Session.updateInvitations(callback,error);
            },error);
        },error);
    };
    
    Session.updateSession = function(callback,error){
        console.log("Session","updateSession");
        Session.updateUserLogin(function(){
            Session.saveSession(function(){
                callback && callback();
                //Session.updateCollections(callback,error);
            },error);
        },error);
    };

    return Session;
}]);

Session.factory('RoomSession', ['$rest','Session','fetch_interval', function($rest, Session, fetch_interval){
    var RoomSession = {};
    
    RoomSession.room = null;
    RoomSession.useraccess = null;
    RoomSession.participants = [];
    RoomSession.messages = [];
    RoomSession.roomaccesspolicy = [];
    RoomSession.fetchInterval = null;
    
    RoomSession.getFromLocalStorage = function(){
        console.log("RoomSession","getFromLocalStorage");
        var item = localStorage['mychat_room_session'];
        
        if(!item) return null;
        
        var useraccess = JSON.parse(item); 
            
        if(useraccess.datetimeOfAccessEnd){
            localStorage.removeItem('mychat_room_session');
            return null;
        }
        else {
            return useraccess;
        }
    };
    
    RoomSession.startSession = function(useraccess,callback,error){
        console.log("RoomSession","startSession");
        $rest.UserAccess.get(useraccess,function(useraccess){
            RoomSession.useraccess = useraccess;
            
            $rest.Room.get({id:useraccess.room},function(room){
                RoomSession.room = room;
                
                RoomSession.saveSession(function(){
                    RoomSession.updateSession(function(){
                        RoomSession.startFetching();
                        callback && callback();
                    },error);
                },error);
            },error);
        },error);
    };
    
    RoomSession.saveSession = function(callback,error){
        console.log("RoomSession","saveSession");
        if(RoomSession.useraccess){
            localStorage['mychat_room_session'] = JSON.stringify(RoomSession.useraccess.toJSON());
        }
        callback && callback();
    };
    
    RoomSession.endSession = function(callback,error){
        console.log("RoomSession","endSession");
        RoomSession.stopFetching();
        RoomSession.useraccess.$terminate(function(){
            localStorage.removeItem('mychat_room_session');
            RoomSession.room = null;
            RoomSession.useraccess = null;
            RoomSession.participants = [];
            RoomSession.messages = [];
            RoomSession.roomaccesspolicy = [];
            callback && callback();
        },error);
    };
    
    RoomSession.startFetching = function(){
        console.log("RoomSession","startFetching");
        if(RoomSession.fetchInterval !== null){
            return;
        }
        
        RoomSession.fetchInterval = setInterval(function(){
            RoomSession.updateSession(function(){

            },function(){
                console.error('RoomSession','error in update');
                RoomSession.stopFetching();
            });
        },fetch_interval);
    };
    
    RoomSession.stopFetching = function(){
        console.log("RoomSession","stopFetching");
        if(RoomSession.fetchInterval === null){
            return;
        }
        
        clearInterval(RoomSession.fetchInterval);
        RoomSession.fetchInterval = null;
    };
    
    RoomSession.updateParticipants = function(callback,error){
        console.log("RoomSession","updateParticipants");
        $rest.Profile.findActivesByRoom({room:RoomSession.room.id},function(participants){
            RoomSession.participants = participants;
            callback && callback();
        },error);
    };
    
    RoomSession.updateMessages = function(callback,error){
        console.log("RoomSession","updateMessages");
        $rest.Message.findByRoom({room:RoomSession.room.id},function(messages){
            RoomSession.messages = messages;
            callback && callback();
        },error);
    };
    
    RoomSession.updateRoomAccessPolicy = function(callback,error){
        console.log("RoomSession","updateRoomAccessPolicy");
        $rest.RoomAccessPolicy.findByRoom({room:RoomSession.room.id},function(roomaccesspolicy){
            RoomSession.roomaccesspolicy = roomaccesspolicy;
            callback && callback();
        },error);
    };
    
    RoomSession.updateSession = function(callback,error){
        console.log("RoomSession","updateSession");
        callback && callback();
        //RoomSession.updateParticipants(function(){
            //RoomSession.updateMessages(function(){
                //RoomSession.updateRoomAccessPolicy(callback,error);
            //},error);
        //},error);
    };
    
    RoomSession.postMessage = function(message,callback,error){
        console.log("RoomSession","postMessage");
        $rest.Message.save({
            body: Session.profile.login + ': ' +message.body,
            owner: Session.profile.id,
            state: 'active',
            room: RoomSession.room.id
        }, function(){
             RoomSession.updateMessages(callback,error);
        }, error);
    };
    
    RoomSession.removeMessage = function(message,callback,error){
        console.log("RoomSession","removeMessage");
        message.state = 'deleted';
        message.$update(callback,error);
    };
    
    RoomSession.enableMessage = function(message,callback,error){
        console.log("RoomSession","enableMessage");
        message.state = 'active';
        message.$update(callback,error);
    };
    
    RoomSession.addUserToBlackList = function(profile,callback,error){
        console.log("RoomSession","addUserToBlackList");
        Session.createRoomAccessPolicy({
            profile: profile.id,
            room: RoomSession.room.id
        },function(){
            RoomSession.updateRoomAccessPolicy(callback,error);
        },error);
    };
    
    return RoomSession;
}]);

