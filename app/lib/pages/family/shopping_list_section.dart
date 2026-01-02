import 'package:flutter/material.dart';

// NEW CODE
class _ShoppingItem {
  final String name;
  final String? quantity;
  bool isCompleted;

  _ShoppingItem({required this.name, this.quantity, this.isCompleted = false});
}

class ShoppingListSection extends StatefulWidget {
  const ShoppingListSection({Key? key}) : super(key: key);

  @override
  _ShoppingListSectionState createState() => _ShoppingListSectionState();
}

class _ShoppingListSectionState extends State<ShoppingListSection> {
  final List<_ShoppingItem> _items = [
    _ShoppingItem(name: 'Trứng', quantity: '1 vỉ'),
    _ShoppingItem(name: 'Sữa tươi', quantity: '2 hộp'),
    _ShoppingItem(name: 'Bánh mì'),
    _ShoppingItem(name: 'Thịt bò', quantity: '300g', isCompleted: true),
    _ShoppingItem(name: 'Rau cải', isCompleted: true),
  ];

  @override
  Widget build(BuildContext context) {
    final uncompletedItems = _items.where((item) => !item.isCompleted).toList();
    final completedItems = _items.where((item) => item.isCompleted).toList();

    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 12.0, vertical: 4.0),
      elevation: 1,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(10.0),
      ),
      child: Padding(
        padding: const EdgeInsets.all(12.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Danh sách mua hàng',
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
            ),
            const SizedBox(height: 8),
            _buildShoppingList('Chưa hoàn thành', uncompletedItems),
            if (completedItems.isNotEmpty) ...[
              const Divider(height: 20, thickness: 1),
              _buildShoppingList('Đã hoàn thành', completedItems),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildShoppingList(String title, List<_ShoppingItem> items) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          title,
          style: TextStyle(
            fontWeight: FontWeight.w600,
            fontSize: 14,
            color: Colors.grey[700],
          ),
        ),
        ListView.builder(
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          itemCount: items.length,
          itemBuilder: (context, index) {
            final item = items[index];
            return Material(
              color: Colors.transparent,
              child: InkWell(
                onTap: () {
                  setState(() {
                    item.isCompleted = !item.isCompleted;
                  });
                },
                child: Padding(
                  padding: const EdgeInsets.symmetric(vertical: 4.0),
                  child: Row(
                    children: [
                      Checkbox(
                        value: item.isCompleted,
                        onChanged: (bool? value) {
                          setState(() {
                            item.isCompleted = value ?? false;
                          });
                        },
                        visualDensity: VisualDensity.compact,
                      ),
                      Expanded(
                        child: Text(
                          item.name,
                          style: TextStyle(
                            decoration: item.isCompleted
                                ? TextDecoration.lineThrough
                                : TextDecoration.none,
                            color: item.isCompleted ? Colors.grey : Colors.black,
                          ),
                        ),
                      ),
                      if (item.quantity != null)
                        Text(
                          item.quantity!,
                          style: TextStyle(
                            color: Colors.grey[600],
                            decoration: item.isCompleted
                                ? TextDecoration.lineThrough
                                : TextDecoration.none,
                          ),
                        ),
                    ],
                  ),
                ),
              ),
            );
          },
        ),
      ],
    );
  }
}
