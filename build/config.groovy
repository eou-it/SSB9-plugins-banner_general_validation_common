
withConfig(configuration) {
    inline(phase: 'CONVERSION') { source, context, classNode ->
        classNode.putNodeMetaData('projectVersion', '0.1')
        classNode.putNodeMetaData('projectName', 'banner_general_validation_common.git')
        classNode.putNodeMetaData('isPlugin', 'true')
    }
}
