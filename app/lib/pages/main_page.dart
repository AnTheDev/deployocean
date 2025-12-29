import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_boilerplate/pages/auth/profile_page.dart';
import 'package:flutter_boilerplate/pages/fridge/fridge_page.dart';
import 'package:flutter_boilerplate/pages/home/home_page.dart';
import 'package:flutter_boilerplate/providers/auth_provider.dart';
import 'package:flutter_boilerplate/providers/fridge_provider.dart';

class MainPage extends StatefulWidget {
  const MainPage({Key? key}) : super(key: key);

  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  int _selectedIndex = 0;

  static const List<Widget> _pages = <Widget>[
    HomePage(),
    PlaceholderWidget(pageName: 'Nhóm'),
    FridgePage(),
    PlaceholderWidget(pageName: 'Thực Đơn'),
    ProfilePage(),
  ];

  void _onItemTapped(int index) {
    // Fetch fridge items when the fridge tab is selected
    if (index == 2) {
      // Assuming familyId is 1 for now. This should come from user data.
      // We use 'context.read' here because we are in a method and don't need to rebuild.
      final authProvider = context.read<AuthProvider>();
      // TODO: Replace hardcoded familyId with the actual family ID from userInfo.
      context.read<FridgeProvider>().fetchFridgeItems(1);
    }
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  void initState() {
    super.initState();
    // Fetch initial data for the first tab (HomePage) if needed
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: IndexedStack(
        index: _selectedIndex,
        children: _pages,
      ),
      bottomNavigationBar: BottomNavigationBar(
        items: const <BottomNavigationBarItem>[
          BottomNavigationBarItem(icon: Icon(Icons.home_outlined), activeIcon: Icon(Icons.home), label: 'Home'),
          BottomNavigationBarItem(icon: Icon(Icons.group_outlined), activeIcon: Icon(Icons.group), label: 'Nhóm'),
          BottomNavigationBarItem(icon: Icon(Icons.kitchen_outlined), activeIcon: Icon(Icons.kitchen), label: 'Tủ lạnh'),
          BottomNavigationBarItem(icon: Icon(Icons.menu_book_outlined), activeIcon: Icon(Icons.menu_book), label: 'Thực Đơn'),
          BottomNavigationBarItem(icon: Icon(Icons.person_outline), activeIcon: Icon(Icons.person), label: 'Tài Khoản'),
        ],
        currentIndex: _selectedIndex,
        selectedItemColor: const Color(0xFFF26F21),
        unselectedItemColor: Colors.grey,
        onTap: _onItemTapped,
        type: BottomNavigationBarType.fixed,
        showUnselectedLabels: true,
      ),
    );
  }
}

class PlaceholderWidget extends StatelessWidget {
  final String pageName;
  const PlaceholderWidget({Key? key, required this.pageName}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(pageName)),
      body: Center(child: Text('Trang $pageName', style: Theme.of(context).textTheme.headlineMedium)),
    );
  }
}
