const calculatePrice = (room, checkIn, checkOut) => {
  let price = room.basePrice;
  const dayOfWeek = checkIn.getDay(); // 0=Sunday, 6=Saturday

  // Weekend multiplier
  if (dayOfWeek === 0 || dayOfWeek === 6) {
    price *= room.dynamicPricing.weekendMultiplier;
  }

  // Peak season example: June - August
  const month = checkIn.getMonth() + 1; // Jan=0
  if (month >= 6 && month <= 8) {
    price *= room.dynamicPricing.peakSeasonMultiplier;
  }

  // Last-minute booking (<2 days)
  const diffTime = checkIn - new Date();
  const diffDays = diffTime / (1000 * 60 * 60 * 24);
  if (diffDays < 2) {
    price *= room.dynamicPricing.lastMinuteMultiplier;
  }

  // Total nights
  const nights = Math.ceil((checkOut - checkIn) / (1000 * 60 * 60 * 24));
  return price * nights;
};

module.exports = calculatePrice;