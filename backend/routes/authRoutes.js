const express = require('express');
const router = express.Router();
const { protect } = require('../middleware/authMiddleware'); // Ensure you have this

// ✅ Updated imports to include getAllStaff
const { loginEmployee, registerGuest, getAllStaff } = require('../controllers/authController');

router.post('/login', loginEmployee);
router.post('/register', registerGuest);

// ✅ NEW ROUTE: Used by Admin to assign tasks
// Note: We use 'protect' so only logged-in users can see staff
router.get('/staff', getAllStaff); 

module.exports = router;