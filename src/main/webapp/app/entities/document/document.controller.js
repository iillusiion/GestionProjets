(function() {
    'use strict';

    angular
        .module('gestionProjetApp')
        .controller('DocumentController', DocumentController);

    DocumentController.$inject = ['$scope', '$state', 'Document', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function DocumentController ($scope, $state, Document, ParseLinks, AlertService, pagingParams, paginationConstants) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();
    
        function loadAll () {
            Document.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.documents = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }
         $scope.search = function (searchQuery) {
            $scope.links = null;
            $scope.page = 1;
            $scope.predicate = 'id';
            $scope.reverse = true;
            $scope.currentSearch = searchQuery;
            $scope.loadAll();
        };

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
