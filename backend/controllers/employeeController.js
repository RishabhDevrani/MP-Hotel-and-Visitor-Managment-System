const Employee = require('../models/Employee');

// Get all staff members (Excluding Admins and Guests)
const getAllStaff = async (req, res) => {
  try {
    const staff = await Employee.find({ 
      role: { $nin: ['admin', 'guest'] } 
    }).select('name username role isActive');
    res.json(staff);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

module.exports = { getAllStaff };