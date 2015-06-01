/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var App = angular.module('App',[
    'ngRoute',
    'Controllers',
    'Rest',
    'Session',
    'Command'
]);

App.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
        when('/home', {
            template: '<div id="HomeView"></div>',
            controller: 'HomeController'
        }).
        when('/profile/:profileId', {
            template: '<div id="ProfileHomeView"></div>',
            controller: 'ProfileHomeController'
        }).
        when('/admin/:profileId', {
            template: '<div id="AdminHomeView"></div>',
            controller: 'AdminHomeController'
        }).        
        when('/room/:roomId/profile/:profileId', {
            template: '<div id="RoomHomeView"></div>',
            controller: 'RoomHomeController'
        }).
        otherwise({
            redirectTo: '/home'
        });
    }
]);

