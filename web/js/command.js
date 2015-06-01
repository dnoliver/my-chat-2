var Command = angular.module('Command', ['Rest','Session']);

Command.factory('Command',[function(){
    function Command(action){
        this.action = action;
    }
    
    Command.prototype.execute = function(options){
      this.action(options);  
    };
    
    return Command;
}]);

Command.factory('VisitantCommand', ['Command', 'Session', '$location',  function(Command, Session, $location){
    function VisitantCommand(CommandManager){
        this.ACTIONS = {};
        
        this.ACTIONS.startSession = new Command(function(options){
            
            if(options === null){
                CommandManager.execute("goToHome");
                return;
            }
            
            if(options.userlogin && options.userlogin.profile ){
                CommandManager.changeImplementation('User');
                CommandManager.execute('startSession',options.userlogin);
                return;
            }
            
            if(options.login && options.password) {
                if($location.search().sessionValidation){
                    Session.createUserLogin(options,function(userlogin){
                        options.login = "";
                        options.password = "";
                        CommandManager.changeImplementation('User');
                        CommandManager.execute("startSession",userlogin);
                    },function(error){
                        console.error(error);
                        alert("invalid login or password");
                    });
                }
                else {
                    var jqxhr = $.get('index.jsp?action=ValidateProfileLoginStatus&profile=' + options.login,function(data){
                        Session.createUserLogin(options,function(userlogin){
                            options.login = "";
                            options.password = "";
                            CommandManager.changeImplementation('User');
                            CommandManager.execute("startSession",userlogin);
                        },function(error){
                            console.error(error);
                            alert("invalid login or password");
                        });
                    }).fail(function(){
                        alert('Your account has been disabled, check email for re enable it');
                        $.get('webresources/userslogins/auth/profile/' + options.login,function(){
                            console.log('mail sent');
                        });
                    });
                }
            }
        });
        
        this.ACTIONS.endSession = new Command(function(options){
            CommandManager.execute("goToHome");
        });
    
        this.ACTIONS.register = new Command(function(profile){
            Session.createProfile(profile,function(profile){
                CommandManager.execute("startSession",profile);
            },function(error){
                console.error(error);
                alert('invalid login');
            });
        });
    
        this.ACTIONS.goToHome = new Command(function(options){
            $location.path() !== '/home'? $location.path('/home') : true;
        });
    };
    
    return VisitantCommand;
}]);

Command.factory('UserCommand', ['Command', 'Session', 'RoomSession','$location', function(Command, Session, RoomSession, $location){
    function UserCommand(CommandManager){
        this.ACTIONS = {};
        
        this.ACTIONS.startSession = new Command(function(userlogin){
            Session.startSession(userlogin,function(){
                
                if(Session.profile.type === 'admin'){
                    CommandManager.changeImplementation('Administrator');
                    CommandManager.execute("startSession",userlogin);
                }
                else {
                    var useraccess = RoomSession.getFromLocalStorage();
                
                    if(useraccess && useraccess.room){
                        CommandManager.changeImplementation('RoomParticipant');
                        CommandManager.execute('startRoomSession',useraccess);
                    }
                    else {
                        CommandManager.execute('goToHome');
                    }
                }
            },function(){
                alert('fail to start user session');
            });
        });
        
        this.ACTIONS.endSession = new Command(function(options){
            Session.endSession(function(){
                CommandManager.changeImplementation('Visitant');
                CommandManager.execute("endSession");
            },function(){
                alert('error in end session');
            });
        });
        
        this.ACTIONS.goToHome = new Command(function(options){
            $location.path('/profile/' + Session.profile.id);
        });
        
        this.ACTIONS.startRoomSession = new Command(function(room){
            Session.createUserAccess(room,function(useraccess){
                CommandManager.changeImplementation('RoomParticipant');
                CommandManager.execute('startRoomSession',useraccess);
            },function(){
                alert('cannot start room session');
            });
        });
    };
    
    return UserCommand;
}]);

Command.factory('AdministratorCommand', ['Command', 'UserCommand', 'Session', 'AdminSession', 'RoomSession', '$location', function(Command, UserCommand, Session, AdminSession, RoomSession, $location){
    function AdministratorCommand(CommandManager){
        UserCommand.apply(this,[CommandManager]);
        
        this.ACTIONS.startSession = new Command(function(userlogin){
            AdminSession.startSession(Session.profile,function(){
                var useraccess = RoomSession.getFromLocalStorage();

                if(useraccess && useraccess.room){
                    CommandManager.changeImplementation('RoomAdministrator');
                    CommandManager.execute('startRoomSession',useraccess);
                }
                else {
                    CommandManager.execute('goToHome');
                }
            },function(){
                // TODO handle error
                alert('error');
            });
        });
        
        this.ACTIONS.endSession = new Command(function(options){
            AdminSession.endSession(function(){
                CommandManager.changeImplementation('User');
                CommandManager.execute("endSession");
            },function(){
                alert('error in end session');
            });
        });
        
        this.ACTIONS.goToHome = new Command(function(options){
            $location.path('/admin/' + Session.profile.id);
        });
        
        this.ACTIONS.startRoomSession = new Command(function(room){
            Session.createUserAccess(room,function(useraccess){
                CommandManager.changeImplementation('RoomAdministrator');
                CommandManager.execute('startRoomSession',useraccess);
            },function(){
                alert('cannot start room session');
            });
       });
    };
    
    return AdministratorCommand;
}]);

Command.factory('RoomParticipantCommand', ['Command', 'UserCommand', 'Session', 'RoomSession', '$location', function(Command, UserCommand, Session, RoomSession ,$location){
    function RoomParticipantCommand(CommandManager){
        UserCommand.apply(this,[CommandManager]);
        
        this.ACTIONS.endSession = new Command(function(options){
            RoomSession.endSession(function(){
                CommandManager.changeImplementation('User');
                CommandManager.execute('endSession');
            },function(){
                alert('error in endSession');
            });
        });
        
        this.ACTIONS.startRoomSession = new Command(function(useraccess){
            RoomSession.startSession(useraccess,function(){
                $location.path('/room/' + RoomSession.room.id + '/profile/' + Session.profile.id);
            },function(){
                // TODO handle startRoomSession error
                alert('error in startRoomSession');
            });
        });
        
        this.ACTIONS.endRoomSession = new Command(function(options){
            RoomSession.endSession(function(){
                CommandManager.changeImplementation('User');
                CommandManager.execute('goToHome');
            },function(){
                alert('error in endRoomSession');
            });
        });
        
        this.ACTIONS.postMessage = new Command(function(message){
            RoomSession.postMessage(message,function(){
                message.body = ""; // to cleanup the input
            },function(){
                alert('error in message post');
            });
        });  
    };
    
    return RoomParticipantCommand;
}]);

Command.factory('RoomAdministratorCommand', ['Command', 'AdministratorCommand', 'RoomSession', '$location', function(Command, AdministratorCommand, RoomSession ,$location){
    function RoomAdministratorCommand(CommandManager){
        AdministratorCommand.apply(this,[CommandManager]);
        
        this.ACTIONS.endSession = new Command(function(options){
            RoomSession.endSession(function(){
                CommandManager.changeImplementation('Administrator');
                CommandManager.execute('endSession');
            },function(){
                alert('error in end session');
            });
        });
        
        this.ACTIONS.startRoomSession = new Command(function(useraccess){
            RoomSession.startSession(useraccess,function(){
                $location.path('/room/' + RoomSession.room.id + '/profile/' + useraccess.profile);
            },function(){
                // TODO handle startRoomSession error
                alert('error start room session');
            });
        });
        
        this.ACTIONS.endRoomSession = new Command(function(options){
            RoomSession.endSession(function(){
                CommandManager.changeImplementation('Administrator');
                CommandManager.execute('goToHome');
            },function(){
                alert('error');
            });
        });

        this.ACTIONS.addUserToBlackList = new Command(function(profile){
            RoomSession.addUserToBlackList({id:profile.id},function(){
                CommandManager.execute('postMessage',{body:profile.login + ' has been removed from this room'});
            },function(){
                alert('error in add user to blacklist');
            });
        });
        
        this.ACTIONS.removeMessage = new Command(function(message){
            RoomSession.removeMessage(message,function(){
                console.log('success in remove message');
            },function(){
                alert('fail in remove message');
            });
        });
        
        this.ACTIONS.enableMessage = new Command(function(message){
            RoomSession.enableMessage(message,function(){
                console.log('success in enable message');
            },function(){
                alert('fail in enable message');
            });
        });
        
        this.ACTIONS.postMessage = new Command(function(message){
            RoomSession.postMessage(message,function(){
                console.log('success in message post');
            },function(){
                alert('error in message post');
            });
        });
    };
    
    return RoomAdministratorCommand;
}]);

Command.factory('CommandImplementation',[
    'VisitantCommand', 
    'UserCommand', 
    'AdministratorCommand',
    'RoomParticipantCommand',
    'RoomAdministratorCommand',
    function(
        VisitantCommand,
        UserCommand,
        AdministratorCommand,
        RoomParticipantCommand,
        RoomAdministratorCommand
    ){
    return {
        Visitant: VisitantCommand,
        User: UserCommand,
        Administrator: AdministratorCommand,
        RoomParticipant: RoomParticipantCommand,
        RoomAdministrator: RoomAdministratorCommand
    };
}]);

Command.factory('CommandManager', [ 'CommandImplementation', function(CommandImplementation){
    
    var CommandManager = {};
    
    var UserImplementation = new CommandImplementation.User(CommandManager);
    var AdministratorImplementation = new CommandImplementation.Administrator(CommandManager);
    var VisitantImplementation = new CommandImplementation.Visitant(CommandManager);
    var RoomParticipantImplementation = new CommandImplementation.RoomParticipant(CommandManager);
    var RoomAdministratorImplementation = new CommandImplementation.RoomAdministrator(CommandManager);
    
    CommandManager.implementationName = null;
    CommandManager.implementation = null;
    
    CommandManager.changeImplementation = function(implementation){
        console.log('CommandManager changeImplementation',implementation);
        switch(implementation){
            case 'Visitant':
                CommandManager.implementation = VisitantImplementation;
                CommandManager.implementationName = implementation;
                break;
            case 'User':
                CommandManager.implementation = UserImplementation;
                CommandManager.implementationName = implementation;
                break;
            case 'Administrator':
                CommandManager.implementation = AdministratorImplementation;
                CommandManager.implementationName = implementation;
                break;
            case 'RoomParticipant':
                CommandManager.implementation = RoomParticipantImplementation;
                CommandManager.implementationName = implementation;
                break;
            case 'RoomAdministrator':
                CommandManager.implementation = RoomAdministratorImplementation;
                CommandManager.implementationName = implementation;
                break;  
            default:
                console.error('CommandManager bad implementation', implementation);      
        }
    };
    
    CommandManager.execute = function(method,options){
        if(CommandManager.implementation.ACTIONS[method]){
            console.log('CommandManager execute',method,'as',CommandManager.implementationName);
            CommandManager.implementation.ACTIONS[method].execute(options);
        }
        else {
            console.log('CommandManager cannot execute',method,'as',CommandManager.implementationName);
            alert('invalid command');
        }
    };
    
    return CommandManager;
}]);