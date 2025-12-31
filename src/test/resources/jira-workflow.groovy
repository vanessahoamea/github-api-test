when 'To Do', {
    'success' should: 'Done'
}

when 'In Progress', {
    'success' should: 'Done'
}

when 'Final', {
    'failure' should: 'To Do'
}