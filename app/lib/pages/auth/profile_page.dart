import 'package:flutter/material.dart';

class ProfilePage extends StatelessWidget {
  const ProfilePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    const orangeColor = Color(0xFFF26F21);

    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        automaticallyImplyLeading: false, // Removes back button
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.symmetric(horizontal: 30.0, vertical: 20.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              const Center(
                child: CircleAvatar(
                  radius: 50,
                  backgroundColor: Color(0xFFE0E0E0),
                  child: Text('Avatar', style: TextStyle(color: Colors.black54)),
                ),
              ),
              const SizedBox(height: 40),
              _buildProfileField(label: 'Họ và tên', value: 'Nguyễn Văn A'),
              _buildProfileField(label: 'Email', value: 'a@gmail.com'),
              _buildProfileField(label: 'Số điện thoại', value: '0123456789'),
              const SizedBox(height: 40),
              SizedBox(
                width: double.infinity,
                height: 56,
                child: ElevatedButton(
                  onPressed: () {
                    // TODO: Navigate to change password page
                  },
                  child: const Text(
                    'Đổi mật khẩu',
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
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                height: 56,
                child: OutlinedButton(
                  onPressed: () {
                    // TODO: Implement logout logic
                  },
                  child: const Text(
                    'Đăng xuất',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  style: OutlinedButton.styleFrom(
                    foregroundColor: orangeColor,
                    side: const BorderSide(color: orangeColor, width: 2),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(28),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
      // You can add your bottom navigation bar here
    );
  }

  Widget _buildProfileField({required String label, required String value}) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            label,
            style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Colors.grey),
          ),
          const SizedBox(height: 8),
          TextField(
            controller: TextEditingController(text: value),
            readOnly: true, // Make it non-editable for display
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
              ),
              contentPadding: const EdgeInsets.symmetric(vertical: 15, horizontal: 15),
            ),
          ),
        ],
      ),
    );
  }
}
