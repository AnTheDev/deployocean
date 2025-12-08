import 'package:flutter/material.dart';

class HomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    const orangeColor = Color(0xFFF26F21);

    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Container(
          width: double.infinity,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              Spacer(flex: 2),
              Container(
                width: 180,
                height: 180,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: Colors.grey[200],
                ),
                // Optional: Add a logo or icon inside the circle
                child: Center(
                  child: Icon(
                    Icons.shopping_cart_outlined, // Example Icon
                    size: 80,
                    color: Colors.grey[400],
                  ),
                ),
              ),
              SizedBox(height: 40),
              Text(
                'Đi chợ thông minh',
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 28,
                  color: orangeColor,
                ),
              ),
              Spacer(flex: 3),
              SizedBox(
                width: 300,
                height: 56,
                child: ElevatedButton(
                  onPressed: () {
                    // TODO: Navigate to login page
                  },
                  child: Text(
                    'Đăng nhập',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: orangeColor,
                    foregroundColor: Colors.white,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(28),
                    ),
                  ),
                ),
              ),
              SizedBox(height: 20),
              SizedBox(
                width: 300,
                height: 56,
                child: OutlinedButton(
                  onPressed: () {
                    // TODO: Navigate to registration page
                  },
                  child: Text(
                    'Đăng kí',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  style: OutlinedButton.styleFrom(
                    foregroundColor: orangeColor,
                    side: BorderSide(color: orangeColor, width: 2),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(28),
                    ),
                  ),
                ),
              ),
              SizedBox(height: 20),
              TextButton(
                onPressed: () {
                  // TODO: Navigate to forgot password page
                },
                child: Text(
                  'Quên mật khẩu?',
                  style: TextStyle(
                    color: orangeColor,
                    fontSize: 16
                  ),
                ),
              ),
              Spacer(flex: 1),
            ],
          ),
        ),
      ),
    );
  }
}
