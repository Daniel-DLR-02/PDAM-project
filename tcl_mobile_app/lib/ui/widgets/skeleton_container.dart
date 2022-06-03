import 'package:flutter/material.dart';
import 'package:skeletons/skeletons.dart';

class SkeletonContainer extends StatelessWidget {
  final double width;
  final double height;
  final double radius;

  const SkeletonContainer._({
    this.width = double.infinity,
    this.height = double.infinity,
    this.radius = 0.0,
    Key? key,
  }) : super(key: key);

  const SkeletonContainer.square({
    width = double.infinity,
    height = double.infinity,
    radius = 0.0,
    Key? key,
  }) : this._(width: width,height:height);

  const SkeletonContainer.imageItem({
    width = double.infinity,
    height = double.infinity,
    radius = 0.0,
    Key? key,
  }) : this._(width: width,height:height,radius: radius);


    const SkeletonContainer.text({
    width = double.infinity,
    height = double.infinity,
    radius = 0.0,
    Key? key,
  }) : this._(width: width,height:height,radius: radius);


  @override
  Widget build(BuildContext context) {
    return SkeletonItem(
      
      child: Container(
        width: width,
        height: height,
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(radius),
        ),
      ),
      
    );
  }
}