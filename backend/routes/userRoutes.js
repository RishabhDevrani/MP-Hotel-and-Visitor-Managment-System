const express = require('express');
const router = express.Router();
const { registerUser, updatePreferences } = require('../controllers/userController');
const { protect } = require('../middleware/authMiddleware');

router.post('/register', registerUser);
router.put('/preferences', protect, updatePreferences);

module.exports = router;