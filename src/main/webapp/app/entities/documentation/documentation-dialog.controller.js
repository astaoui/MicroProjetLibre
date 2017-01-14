(function() {
    'use strict';

    angular
        .module('projectMicroServicesApp')
        .controller('DocumentationDialogController', DocumentationDialogController);

    DocumentationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Documentation', 'Tag', 'Project', 'Deliverable'];

    function DocumentationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Documentation, Tag, Project, Deliverable) {
        var vm = this;

        vm.documentation = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.tags = Tag.query();
        vm.projects = Project.query();
        vm.deliverables = Deliverable.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.documentation.id !== null) {
                Documentation.update(vm.documentation, onSaveSuccess, onSaveError);
            } else {
                Documentation.save(vm.documentation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectMicroServicesApp:documentationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFile = function ($file, documentation) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        documentation.file = base64Data;
                        documentation.fileContentType = $file.type;
                    });
                });
            }
        };

    }
})();
