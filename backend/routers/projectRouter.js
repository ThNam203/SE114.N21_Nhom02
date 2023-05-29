const express = require('express')
const projectController = require('../controllers/projectController')

const router = express.Router({
    mergeParams: true,
})

router
    .route('/')
    .get(projectController.getAllProjectOfUser)
    .post(projectController.saveNewProject)

router.route('/:projectId').get(projectController.getProjectById)

router.route('/:projectId/board').post(projectController.createAndGetNewBoard)

module.exports = router
