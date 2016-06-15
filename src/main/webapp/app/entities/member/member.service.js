(function() {
    'use strict';
    angular
        .module('versushipsterApp')
        .factory('Member', Member);

    Member.$inject = ['$resource', 'DateUtils'];

    function Member ($resource, DateUtils) {
        var resourceUrl =  'api/members/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.memberFrom = DateUtils.convertLocalDateFromServer(data.memberFrom);
                        data.memberTo = DateUtils.convertLocalDateFromServer(data.memberTo);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.memberFrom = DateUtils.convertLocalDateToServer(data.memberFrom);
                    data.memberTo = DateUtils.convertLocalDateToServer(data.memberTo);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.memberFrom = DateUtils.convertLocalDateToServer(data.memberFrom);
                    data.memberTo = DateUtils.convertLocalDateToServer(data.memberTo);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
