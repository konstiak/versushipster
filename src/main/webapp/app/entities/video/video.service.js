(function() {
    'use strict';
    angular
        .module('versushipsterApp')
        .factory('Video', Video);

    Video.$inject = ['$resource'];

    function Video ($resource) {
        var resourceUrl =  'api/videos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
