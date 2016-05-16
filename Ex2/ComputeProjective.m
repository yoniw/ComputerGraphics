function [ TransformedIm ] = ComputeProjective( im, H )
    
    TransformedIm = imwarp(im, projective2d(H));

end


