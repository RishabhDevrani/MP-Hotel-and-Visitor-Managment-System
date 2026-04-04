const express = require('express');
const router = express.Router();
// Ensure these names match your taskController.js exactly
const { assignTask, getMyTasks, completeTask } = require('../controllers/taskController');
const { protect, authorize } = require('../middleware/authMiddleware');

// 1. Assign Task (Admin Only)
router.post('/', protect, authorize('admin'), assignTask);

// 2. View My Tasks (Staff)
router.get('/my-tasks', protect, getMyTasks);

// 3. Complete Task (Staff)
// Changed :taskId to :id to ensure compatibility with most controllers
router.put('/:id/complete', protect, completeTask);

module.exports = router;