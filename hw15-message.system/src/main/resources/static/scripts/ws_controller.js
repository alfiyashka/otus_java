function WsCtrl($scope) {
    var vm = this;
    vm.users = [];

    angular.element('.modal').modal();
    setConnect();

    function setConnectedElements() {
        angular.element(".manipulate").toggleClass("disabled");
    }

    function setConnect() {
        stompClient = Stomp.over(new SockJS('/hw-ms-websocket'));
        stompClient.connect({}, function (frame) {
            setConnectedElements();
            sendConnect();
            console.log('connected: ' + frame);
            stompClient.subscribe('/topic/response', function (msg) {
                vm.setUsers(JSON.parse(msg.body).users)
            });
        });
    }

    vm.connectWs = function () {
        setConnect();
    };

    function sendConnect() {
        stompClient.send("/app/connect", {}, JSON.stringify({'method': 'connect'}));
    }

    vm.submitUser = function () {
        stompClient.send("/app/save", {}, JSON.stringify({'user': $scope.userForm}));
        _clearFormData();
    };


    vm.disconnectWs = function () {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        setConnectedElements();
        console.log("Disconnected");
    };

    vm.setUsers = function (users) {
        vm.users = users;
        $scope.$apply();
    };

    function _clearFormData() {
        $scope.userForm.name = "";
        $scope.userForm.age = null;
        $scope.userForm.address = "";
        $scope.userForm.phone = "";
    }
}

angular
    .module('UserWsApp', [])
    .controller('WsCtrl', WsCtrl);
