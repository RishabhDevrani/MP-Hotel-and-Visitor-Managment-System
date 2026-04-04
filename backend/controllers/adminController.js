const Employee = require('../models/Employee');
const Room = require('../models/Room');
const bcrypt = require('bcrypt');

// @desc    Hire a new employee
// @route   POST /api/admin/employee
// @access  Private (Admin Only)
const hireEmployee = async (req, res) => {
  const { name, username, password, role, department } = req.body;

  try {
    // 1. Check if username is taken
    const employeeExists = await Employee.findOne({ username });
    if (employeeExists) {
      return res.status(400).json({ message: 'Username already exists in the system' });
    }

    // 2. Hash their new password
    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(password, salt);

    // 3. Create the employee
    const employee = await Employee.create({
      name,
      username,
      password: hashedPassword,
      role,
      department
    });

    res.status(201).json({
      message: `${role} account created successfully for ${name}`,
      employeeId: employee._id
    });
  } catch (error) {
    res.status(500).json({ message: 'Server Error', error: error.message });
  }
};

// @desc    Create a new hotel room
// @route   POST /api/admin/room
// @access  Private (Admin Only)
const createRoom = async (req, res) => {
  const { roomNumber, type, basePrice } = req.body;

  try {
    // 1. Check if room already exists
    const roomExists = await Room.findOne({ roomNumber });
    if (roomExists) {
      return res.status(400).json({ message: `Room ${roomNumber} already exists` });
    }

    // 2. Create the room
    const room = await Room.create({
      roomNumber,
      type,
      basePrice
    });

    res.status(201).json({
      message: `${type} Room ${roomNumber} officially added to the hotel`,
      roomId: room._id
    });
  } catch (error) {
    res.status(500).json({ message: 'Server Error', error: error.message });
  }
};
const getAllRooms = async (req, res) => {
  try {
    const rooms = await Room.find({});
    res.json(rooms);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getAllStaff = async (req, res) => {
  try {
    // We only want to assign tasks to 'cleaner' or 'chef' roles
    const staff = await Employee.find({ role: { $ne: 'admin' } }).select('name username role');
    res.json(staff);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Update your exports
module.exports = { hireEmployee, createRoom, getAllRooms, getAllStaff };