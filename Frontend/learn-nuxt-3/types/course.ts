export interface Course {
  title: string;
  subtitle: string;
  courseSlug: string;
  content: string;
  thumbnail: string;
  video: string;
  rating: number;
  reviewsCount: number;
  studentCount: number;
  reviewsUrl: string;
  inflearnUrl: string;
  gymcodingUrl: string;
}

// 특성 속성을 제외하고 새로운 속성을 만들 경우 Omit 사용
export interface CourseWithPath
  extends Omit<Course, 'rating' | 'reviewsCount' | 'studentCount'> {
  path: string;
  rating: string;
  reviewsCount: string;
  studentCount: string;
}
