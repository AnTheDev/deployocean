import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_boilerplate/providers/fridge_provider.dart';
import 'package:flutter_boilerplate/providers/family_provider.dart';
import 'package:intl/intl.dart';

// FIX: Đổi FRIDGE thành COOLER để khớp với backend
enum FridgeLocation { FREEZER, COOLER, PANTRY }

class AddFridgeItemPage extends StatefulWidget {
  const AddFridgeItemPage({Key? key}) : super(key: key);

  @override
  _AddFridgeItemPageState createState() => _AddFridgeItemPageState();
}

class _AddFridgeItemPageState extends State<AddFridgeItemPage> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _quantityController = TextEditingController();
  final _unitController = TextEditingController();
  DateTime? _expirationDate;
  FridgeLocation _location = FridgeLocation.COOLER;

  @override
  void dispose() {
    _nameController.dispose();
    _quantityController.dispose();
    _unitController.dispose();
    super.dispose();
  }

  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _expirationDate ?? DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime(2101),
    );
    if (picked != null && picked != _expirationDate) {
      setState(() => _expirationDate = picked);
    }
  }

  void _submitForm() {
    if (_formKey.currentState!.validate()) {
      final familyProvider = context.read<FamilyProvider>();
      final familyId = familyProvider.selectedFamily?.id;

      if (familyId == null) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Lỗi: Không tìm thấy ID gia đình.'), backgroundColor: Colors.red));
        return;
      }

      // FIX: Send quantity as a String to match BigDecimal on the backend.
      final itemData = {
        'familyId': familyId,
        'customProductName': _nameController.text,
        'quantity': _quantityController.text, // Send as String
        'unit': _unitController.text,
        'expirationDate': _expirationDate?.toIso8601String().split('T').first,
        'location': _location.toString().split('.').last,
      };

      // Keep the rest of the logic the same
      context.read<FridgeProvider>().addFridgeItem(itemData).then((_) {
        if (mounted) {
          Navigator.of(context).pop();
        }
      }).catchError((e) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(e.toString()), backgroundColor: Colors.red));
        }
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Thêm Thực Phẩm Mới')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: <Widget>[
              // Ghi chú về trường bắt buộc
              const Padding(
                padding: EdgeInsets.only(bottom: 16.0),
                child: Text(
                  '(*) Trường bắt buộc',
                  style: TextStyle(color: Colors.red, fontSize: 12),
                ),
              ),
              TextFormField(
                controller: _nameController,
                decoration: const InputDecoration(
                  labelText: 'Tên thực phẩm *',
                  hintText: 'VD: Thịt bò, Rau cải, Sữa tươi...',
                  helperText: 'Nhập tên thực phẩm bạn muốn thêm vào tủ lạnh',
                ),
                validator: (v) => v == null || v.trim().isEmpty ? 'Vui lòng nhập tên thực phẩm' : null,
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _quantityController,
                decoration: const InputDecoration(
                  labelText: 'Số lượng *',
                  hintText: 'VD: 1, 2.5, 500...',
                  helperText: 'Chỉ nhập số (có thể dùng số thập phân)',
                ),
                keyboardType: const TextInputType.numberWithOptions(decimal: true),
                validator: (v) {
                  if (v == null || v.trim().isEmpty) {
                    return 'Vui lòng nhập số lượng';
                  }
                  final number = double.tryParse(v.trim());
                  if (number == null) {
                    return 'Số lượng phải là số (VD: 1, 2.5, 100)';
                  }
                  if (number <= 0) {
                    return 'Số lượng phải lớn hơn 0';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _unitController,
                decoration: const InputDecoration(
                  labelText: 'Đơn vị *',
                  hintText: 'VD: kg, lít, gói, hộp, quả...',
                  helperText: 'Đơn vị tính của thực phẩm',
                ),
                validator: (v) => v == null || v.trim().isEmpty ? 'Vui lòng nhập đơn vị' : null,
              ),
              const SizedBox(height: 16),
              DropdownButtonFormField<FridgeLocation>(
                value: _location,
                decoration: const InputDecoration(
                  labelText: 'Vị trí *',
                  border: OutlineInputBorder(),
                  helperText: 'Chọn nơi lưu trữ thực phẩm',
                ),
                items: FridgeLocation.values.map((loc) {
                  String displayName;
                  switch (loc) {
                    case FridgeLocation.FREEZER:
                      displayName = 'Ngăn đông';
                      break;
                    case FridgeLocation.COOLER:
                      displayName = 'Ngăn mát';
                      break;
                    case FridgeLocation.PANTRY:
                      displayName = 'Kệ bếp';
                      break;
                  }
                  return DropdownMenuItem(value: loc, child: Text(displayName));
                }).toList(),
                onChanged: (val) => setState(() => _location = val!),
              ),
              const SizedBox(height: 16),
              InputDecorator(
                decoration: const InputDecoration(
                  labelText: 'Ngày hết hạn (tùy chọn)',
                  border: OutlineInputBorder(),
                  helperText: 'Giúp theo dõi và cảnh báo khi thực phẩm sắp hết hạn',
                ),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      _expirationDate == null
                          ? 'Chưa chọn'
                          : DateFormat('dd/MM/yyyy').format(_expirationDate!),
                      style: TextStyle(
                        color: _expirationDate == null ? Colors.grey : null,
                      ),
                    ),
                    Row(
                      children: [
                        if (_expirationDate != null)
                          IconButton(
                            icon: const Icon(Icons.clear, size: 20),
                            onPressed: () => setState(() => _expirationDate = null),
                            tooltip: 'Xóa ngày',
                          ),
                        TextButton.icon(
                          onPressed: () => _selectDate(context),
                          icon: const Icon(Icons.calendar_today, size: 18),
                          label: const Text('Chọn ngày'),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 32),
              ElevatedButton.icon(
                onPressed: _submitForm,
                icon: const Icon(Icons.add),
                label: const Text('Thêm Thực Phẩm'),
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
