const User = require('../models/User');

// @desc    Register a new Guest
const registerUser = async (req, res) => {
  const { name, email, phone } = req.body;
  try {
    const user = await User.create({ name, email, phone });
    res.status(201).json(user);
  } catch (error) {
    res.status(500).json({ message: "Registration failed", error: error.message });
  }
};

// @desc    Update VIP Preferences (Pillows, Temp, etc.)
const updatePreferences = async (req, res) => {
  const { userId, preferences } = req.body;
  try {
    const user = await User.findByIdAndUpdate(
      userId, 
      { $set: { preferences } }, 
      { new: true }
    );
    res.json({ message: "Preferences updated!", user });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

module.exports = { registerUser, updatePreferences };