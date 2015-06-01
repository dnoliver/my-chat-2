var Rest = angular.module('Rest', ['ngResource']);

Rest.factory('Profile', ['$resource', function($resource){
    return $resource('webresources/profiles/:id', {id: '@id'}, {
        get: {method:'GET'},
        save: {method:'POST'},
        update: {method:'PUT'},
        delete: {method:'DELETE'},
        
        query: {method:'GET', isArray:true},
        findByLogin: {method:'GET', url:'webresources/profiles/login/:login'},
        findActivesByRoom: {method:'GET', url:'webresources/profiles/room/:room/actives', isArray:true}
    });
}]);

Rest.factory('UserLogin', ['$resource', function($resource){
    return $resource('webresources/userslogins/:id', {id: '@id'}, {
        get: {method:'GET'},
        save: {method:'POST'},
        update: {method:'PUT'},
        delete: {method:'DELETE'},
        
        query: {method:'GET', isArray:true},
        findActiveByLogin: {method:'GET',url:'webresources/userslogins/login/:login'},
        
        terminate: {method:'POST', url:'webresources/userslogins/:id/terminate'}
    });        
}]);

Rest.factory('UserAccess', ['$resource', function($resource){
    return $resource('webresources/usersaccess/:id', {id: '@id'}, {
        get: {method:'GET'},
        save: {method:'POST'},
        update: {method:'PUT'},
        delete: {method:'DELETE'},
        
        query: {method:'GET', isArray:true},
        findActivesByRoom: {method:'GET', url: 'webresources/usersaccess/room/:room/actives', isArray:true },
        
        terminate: {method:'POST', url: 'webresources/usersaccess/:id/terminate'}
    });        
}]);

Rest.factory('Room', ['$resource', function($resource){
    return $resource('webresources/rooms/:id', {id: '@id'}, {
        get: {method:'GET'},
        save: {method:'POST'},
        update: {method:'PUT'},
        delete: {method:'DELETE'},
        
        query: {method:'GET', isArray:true},
        findPublics: {method:'GET', url:'webresources/rooms/type/public', isArray:true},
        findByOwner: {method:'GET', url:'webresources/rooms/owner/:owner', isArray:true}
    });        
}]);

Rest.factory('Invitation', ['$resource', function($resource){
    return $resource('webresources/invitations/:id', {id: '@id'}, {
        get: {method:'GET'},
        save: {method:'POST'},
        update: {method:'PUT'},
        delete: {method:'DELETE'},
        
        query: {method:'GET', isArray:true},
        findBySender: {method:'GET', url:'webresources/invitations/sender/:sender', isArray:true},
        findByReceiver: {method:'GET', url:'webresources/invitations/receiver/:receiver', isArray:true}
    });        
}]);

Rest.factory('Message', ['$resource', function($resource){
    return $resource('webresources/messages/:id', {id: '@id'}, {
        get: {method:'GET'},
        save: {method:'POST'},
        update: {method:'PUT'},
        delete: {method:'DELETE'},
        
        query: {method:'GET', isArray:true},
        findByRoom: {method:'GET', url:'webresources/messages/room/:room', isArray:true},
        findByOwner: {method:'GET', url:'webresources/messages/owner/:owner', isArray:true}
    });        
}]);

Rest.factory('RoomAccessPolicy', ['$resource', function($resource){
    return $resource('webresources/roomsaccesspolicy/:id', { id: '@id'}, {
        query: {method:'GET', isArray:true},
        
        get: {method:'GET'},
        save: {method:'POST'},
        update: {method:'PUT'},
        delete: {method:'DELETE'},
        
        findByRoom: {method:'GET', url:'webresources/roomsaccesspolicy/room/:room', isArray:true}
    });        
}]);

Rest.factory('$rest', ['Profile','UserLogin','UserAccess','Room','Invitation','Message','RoomAccessPolicy', function(Profile,UserLogin,UserAccess,Room,Invitation,Message,RoomAccessPolicy){
    return {
        Profile: Profile,
        UserLogin: UserLogin,
        Room: Room,
        Invitation: Invitation,
        UserAccess: UserAccess,
        Message: Message,
        RoomAccessPolicy: RoomAccessPolicy
    };
}]);



