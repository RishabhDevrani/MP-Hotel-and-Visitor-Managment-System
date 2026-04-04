const express = require('express');
const router = express.Router();
const { createBooking, getAvailableRooms, checkout } = require('../controllers/bookingController');
const { protect } = require('../middleware/authMiddleware');

// 1. Guests can see what's available
router.get('/available', getAvailableRooms);

// 2. Guests can book a room
router.post('/book', protect, createBooking);

// 3. Guests can check out
router.put('/:bookingId/checkout', protect, checkout);

module.exports = router;