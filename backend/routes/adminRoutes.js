const express = require('express');
const router = express.Router();

// ✅ Fixed: Added getAllStaff to the list of imports below
const { hireEmployee, createRoom, getAllRooms, getAllStaff } = require('../controllers/adminController');
const { protect, authorize } = require('../middleware/authMiddleware');

// Every route in this file must pass through TWO bouncers:
// 1. protect (Are you logged in?)
// 2. authorize('admin') (Are you specifically an Admin?)

router.post('/employee', protect, authorize('admin'), hireEmployee);
router.post('/room', protect, authorize('admin'), createRoom);

// This lets the Dashboard see all rooms
router.get('/rooms', protect, authorize('admin'), getAllRooms);

// This lets the Dashboard see all staff for task assignment
router.get('/staff', protect, authorize('admin'), getAllStaff);

module.exports = router;