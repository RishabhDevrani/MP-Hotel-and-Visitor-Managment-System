const Employee = require('../models/Employee');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

// Helper to generate access tokens
const generateToken = (id, role) => {
  return jwt.sign({ id, role }, process.env.JWT_SECRET, {
    expiresIn: '30d',
  });
};

// @desc    Authenticate User (Admin, Staff, or Guest)
// @route   POST /api/auth/login
const loginEmployee = async (req, res) => {
  const { username, password } = req.body;

  try {
    const employee = await Employee.findOne({ username });

    if (employee && (await bcrypt.compare(password, employee.password))) {
      // Check if account is active (Important for staff management)
      if (employee.isActive === false) {
        return res.status(403).json({ message: 'Account deactivated. Please contact administration.' });
      }

      res.json({
        _id: employee._id,
        name: employee.name,
        username: employee.username,
        role: employee.role,
        department: employee.department || 'Guest Services', 
        token: generateToken(employee._id, employee.role),
      });
    } else {
      res.status(401).json({ message: 'Access Denied: Invalid credentials.' });
    }
  } catch (error) {
    res.status(500).json({ message: 'Server Error', error: error.message });
  }
};

// @desc    Register a new Guest
// @route   POST /api/auth/register
const registerGuest = async (req, res) => {
  const { name, username, password } = req.body;

  try {
    // 1. Check if username is already taken
    const userExists = await Employee.findOne({ username });
    if (userExists) {
      return res.status(400).json({ message: 'This System ID is already in use.' });
    }

    // 2. Create the guest
    // Note: Ensure your Employee Model handles password hashing via .pre('save')
    const guest = await Employee.create({
      name,
      username,
      password,
      role: 'guest',           // Explicitly set as guest
      department: 'Patron',    // Fills required department field
      isActive: true           // Ensures they can log in immediately
    });

    // 3. Return data + token so they could be logged in immediately if desired
    res.status(201).json({
      _id: guest._id,
      name: guest.name,
      username: guest.username,
      role: guest.role,
      token: generateToken(guest._id, guest.role)
    });

  } catch (error) {
    // This log is your best friend—it tells you why the 500 error is happening
    console.error("Critical Registration Error:", error); 
    res.status(500).json({ 
      message: 'Registration failed at the Front Desk.', 
      error: error.message 
    });
  }
};
// Add this new function to authController.js
const getAllStaff = async (req, res) => {
  try {
    // ✅ Filter: Role must NOT be 'admin' AND must NOT be 'guest'
    const staff = await Employee.find({ 
      role: { $nin: ['admin', 'guest'] } 
    }).select('name username role isActive');

    res.json(staff);
  } catch (error) {
    res.status(500).json({ message: "Failed to fetch staff list", error: error.message });
  }
};

// ✅ UPDATE your exports at the very bottom
module.exports = { loginEmployee, registerGuest, getAllStaff };