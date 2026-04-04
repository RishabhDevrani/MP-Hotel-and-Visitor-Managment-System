const mongoose = require('mongoose');
const dotenv = require('dotenv');
const bcrypt = require('bcrypt');
const connectDB = require('./config/db');
const Employee = require('./models/Employee');

dotenv.config();
connectDB();

const seedAdmin = async () => {
  try {
    const adminExists = await Employee.findOne({ role: 'admin' });
    
    if (adminExists) {
      console.log('An Admin already exists in the database. Seeding aborted.');
      process.exit();
    }

    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash('MasterKey7Star!', salt);

    const masterAdmin = new Employee({
      name: 'System Grandmaster',
      username: 'admin01',
      password: hashedPassword,
      role: 'admin',
      department: 'Executive Board',
    });

    await masterAdmin.save();
    console.log('✅ Master Admin Account successfully injected into the database!');
    process.exit();
    
  } catch (error) {
    console.error(`❌ Error seeding database: ${error.message}`);
    process.exit(1);
  }
};

seedAdmin();